package sg.nus.iss.javaspring.adprojrct.Controllers;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.ResultCallback;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nus.iss.javaspring.adprojrct.Models.Transaction;
import sg.nus.iss.javaspring.adprojrct.Services.TransactionService;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/jky")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    public TransactionController() {
        com.alibaba.dashscope.utils.Constants.apiKey = "sk-02fe14062b1843688defb960e9c1ff78";
    }

    @GetMapping("/Tips/{userId}")
    public ResponseEntity<List<Map<String, String>>> generateBudgetAdjustmentTips(@PathVariable int userId) throws ApiException, NoApiKeyException, UploadFileException, IOException, InputRequiredException, InterruptedException {
        // 生成CSV文件
        Path tempFilePath = Files.createTempFile("expenses_", ".csv");
        String csvFilePath = tempFilePath.toAbsolutePath().toString();

        generateCSVFile(userId, csvFilePath);

        // 调用通义千问API
        String tips = callTongYiQianWenAPI(csvFilePath);

        // 将结果解析为JSON格式
        List<Map<String, String>> jsonResponse = parseTipsToJson(tips);

        //Files.delete(tempFilePath);

        return ResponseEntity.ok(jsonResponse);
    }

    private void generateCSVFile(int userId, String csvFilePath) throws IOException {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(csvFilePath), CSVFormat.DEFAULT.withHeader("ID", "Amount", "Description", "Category", "Date"))) {
            for (Transaction transaction : transactions) {
                printer.printRecord(transaction.getId(), transaction.getAmount(), transaction.getDescription(), transaction.getCategory().getName(), transaction.getCreated_at());
            }
        }
    }

    private String callTongYiQianWenAPI(String csvFilePath) throws ApiException, NoApiKeyException, UploadFileException, IOException, InputRequiredException, InterruptedException {
        // 读取CSV文件
        List<Map<String, Object>> csvData = readCSVFile(csvFilePath);

        // 准备内容
        List<Map<String, Object>> contentList = new ArrayList<>();
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("csv_data", csvData);
        contentMap.put("text", "输出读取到的内容");
        contentList.add(contentMap);

        // 调用API
        Generation gen = new Generation();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("Generate Spending Overview, Budget Adjustment, and Savings Tips based on the following CSV file contents:" + contentList)
                .build();
        GenerationParam param = GenerationParam.builder()
                .model("qwen-max")
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(0.8)
                .incrementalOutput(true)
                .build();

        Semaphore semaphore = new Semaphore(0);
        StringBuilder fullContent = new StringBuilder();
        gen.streamCall(param, new ResultCallback<GenerationResult>() {

            @Override
            public void onEvent(GenerationResult message) {
                fullContent.append(message.getOutput().getChoices().get(0).getMessage().getContent());
            }

            @Override
            public void onError(Exception err) {
                semaphore.release();
            }

            @Override
            public void onComplete() {
                semaphore.release();
            }
        });

        semaphore.acquire();
        return fullContent.toString();
    }

    private List<Map<String, Object>> readCSVFile(String csvFilePath) throws IOException {
        FileReader reader = new FileReader(csvFilePath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        List<Map<String, Object>> csvData = new ArrayList<>();

        for (CSVRecord record : records) {
            Map<String, Object> recordMap = new HashMap<>();
            recordMap.put("ID", Integer.parseInt(record.get("ID")));
            recordMap.put("Amount", Double.parseDouble(record.get("Amount")));
            recordMap.put("Description", record.get("Description"));
            recordMap.put("Category", record.get("Category"));
            recordMap.put("Date", record.get("Date"));
            csvData.add(recordMap);
        }

        return csvData;
    }

    private List<Map<String, String>> parseTipsToJson(String tips) {
        List<Map<String, String>> result = new ArrayList<>();

        System.out.println(tips);

        String spendingOverview = extractSection(tips, "Spending Overview", "Budget Adjustment");
        String budgetAdjustment = extractSection(tips, "Budget Adjustment", "Savings Tips");
        String savingsTips = extractSection(tips, "Savings Tips", null);

        result.add(createJsonEntry("Spending Overview", spendingOverview));
        result.add(createJsonEntry("Budget Adjustment Suggestions", budgetAdjustment));
        result.add(createJsonEntry("Savings Tips", savingsTips));

        return result;
    }

    private String extractSection(String tips, String startTag, String endTag) {
        int startIndex = tips.indexOf(startTag);
        int endIndex = endTag != null ? tips.indexOf(endTag) : tips.length();

        if (startIndex != -1 && endIndex > startIndex) {
            String section = tips.substring(startIndex + startTag.length(), endIndex).trim();
            System.out.println("Extracted Section: " + section); // 调试输出
            return section;
        }
        return "No data found for " + startTag;
    }


    private Map<String, String> createJsonEntry(String name, String value) {
        Map<String, String> entry = new HashMap<>();
        entry.put("name", name);
        entry.put("value", value);
        return entry;
    }
}
