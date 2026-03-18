package com.example.demo.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.AI;
import com.example.demo.utils.HS256Util;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HexFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author gaoxianhua
 */
@RestController
@RequestMapping("/ai")
public class AIController {
    @Value("${tencent.SECRET_ID}")
    private String SECRET_ID;
    @Value("${tencent.SECRET_KEY}")
    private String SECRET_KEY;

//    @Value("${baidu.API_KRY}")
//    private String BAIDU_API_KEY;
//    @Value("${baidu.SECRET_KRY}")
//    private String BAIDU_SECRET_KEY;


    @ApiOperation(value = "图片变清晰, 图片地址转化base64")
    @PostMapping("/base64/image/inFocus")
    public Result base64ImageInFocusApi(@RequestBody String url) {

        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String action = "EnhanceImage";
        String host = "tiia.tencentcloudapi.com";
        String version = "2019-05-29";
        String service = "tiia";
        String region = "ap-beijing";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 注意时区，否则容易出错
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.valueOf((timestamp + "000"))));
        String algorithm = "TC3-HMAC-SHA256";

        // ************* 步骤 0：图片地址转化base64 *************
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // ************* 步骤 1：拼接规范请求串 *************
        String httpRequestMethod = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json\n"
                + "host:" + host + "\n" + "x-tc-action:" + action.toLowerCase() + "\n";
        String signedHeaders = "content-type;host;x-tc-action";

        String payload = "{\"ImageBase64\":\"" + base64Image + "\"}";
        try {
            String hashedRequestPayload = HS256Util.sha256Hex(payload);
            String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                    + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

            // ************* 步骤 2：拼接待签名字符串 *************
            String credentialScope = date + "/" + service + "/" + "tc3_request";
            String hashedCanonicalRequest = HS256Util.sha256Hex(canonicalRequest);
            String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

            // ************* 步骤 3：计算签名 *************
            byte[] secretDate = HS256Util.hmac256(("TC3" + SECRET_KEY).getBytes(HS256Util.UTF8), date);
            byte[] secretService = HS256Util.hmac256(secretDate, service);
            byte[] secretSigning = HS256Util.hmac256(secretService, "tc3_request");
            String signature = HexFormat.of().formatHex(HS256Util.hmac256(secretSigning, stringToSign));

            // ************* 步骤 4：拼接 Authorization *************
            String authorization = algorithm + " " + "Credential=" + SECRET_ID + "/" + credentialScope + ", "
                    + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            headers.set("Content-Type", "application/json");
            headers.set("Host", host);
            headers.set("X-TC-Action", action);
            headers.set("X-TC-Timestamp", timestamp);
            headers.set("X-TC-Version", version);
            headers.set("X-TC-Region", region);
            headers.set("X-TC-Language", "zh-CN");

            StringBuilder sb = new StringBuilder();
            sb.append("curl -X POST https://").append(host)
                    .append(" -H \"Authorization: ").append(authorization).append("\"")
                    .append(" -H \"Content-Type: application/json\"")
                    .append(" -H \"Host: ").append(host).append("\"")
                    .append(" -H \"X-TC-Action: ").append(action).append("\"")
                    .append(" -H \"X-TC-Timestamp: ").append(timestamp).append("\"")
                    .append(" -H \"X-TC-Version: ").append(version).append("\"")
                    .append(" -H \"X-TC-Region: ").append(region).append("\"")
                    .append(" -H \"X-TC-Language: zh-CN\"")
                    .append(" -d '").append(payload).append("'");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity("https://tiia.tencentcloudapi.com", requestEntity, String.class);

            // 处理响应
            if (response.getStatusCode().is2xxSuccessful()) {

                String responseBody = response.getBody();
                JSONObject obj = JSONObject.parseObject(responseBody);
                JSONObject data = obj.getJSONObject("Response");
                if (data.get("Error") == null) {
                    data.put("oldImage", url);
                    return Result.success(data);
                } else {
                    return Result.fail(data.getJSONObject("Error").getString("Message"));
                }
            } else {
                System.out.println("Error: " + response.getStatusCodeValue());
                return Result.fail( response.getStatusCodeValue());
            }


        } catch (Exception e) {
            return Result.fail(50000, e.getMessage());
        }
    }

    @ApiOperation(value = "人像动漫化, 传base64")
    @PostMapping("/base64/generative/animation")
    public Result base64PortraitsGenerateAnimeCharacters(@RequestParam String base64Image) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String action = "FaceCartoonPic";
        String host = "ft.tencentcloudapi.com";
        String version = "2020-03-04";
        String service = "ft";
        String region = "ap-nanjing";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 注意时区，否则容易出错
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.valueOf((timestamp + "000"))));
        String algorithm = "TC3-HMAC-SHA256";

        // ************* 步骤 1：拼接规范请求串 *************
        String httpRequestMethod = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json\n"
                + "host:" + host + "\n" + "x-tc-action:" + action.toLowerCase() + "\n";
        String signedHeaders = "content-type;host;x-tc-action";

        String payload = "{\"Image\":\"" + base64Image + "\"}";
        try {
            String hashedRequestPayload = HS256Util.sha256Hex(payload);
            String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                    + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

            // ************* 步骤 2：拼接待签名字符串 *************
            String credentialScope = date + "/" + service + "/" + "tc3_request";
            String hashedCanonicalRequest = HS256Util.sha256Hex(canonicalRequest);
            String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

            // ************* 步骤 3：计算签名 *************
            byte[] secretDate = HS256Util.hmac256(("TC3" + SECRET_KEY).getBytes(HS256Util.UTF8), date);
            byte[] secretService = HS256Util.hmac256(secretDate, service);
            byte[] secretSigning = HS256Util.hmac256(secretService, "tc3_request");
            String signature = HexFormat.of().formatHex(HS256Util.hmac256(secretSigning, stringToSign));

            // ************* 步骤 4：拼接 Authorization *************
            String authorization = algorithm + " " + "Credential=" + SECRET_ID + "/" + credentialScope + ", "
                    + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            headers.set("Content-Type", "application/json");
            headers.set("Host", host);
            headers.set("X-TC-Action", action);
            headers.set("X-TC-Timestamp", timestamp);
            headers.set("X-TC-Version", version);
            headers.set("X-TC-Region", region);
            headers.set("X-TC-Language", "zh-CN");

            StringBuilder sb = new StringBuilder();
            sb.append("curl -X POST https://").append(host)
                    .append(" -H \"Authorization: ").append(authorization).append("\"")
                    .append(" -H \"Content-Type: application/json\"")
                    .append(" -H \"Host: ").append(host).append("\"")
                    .append(" -H \"X-TC-Action: ").append(action).append("\"")
                    .append(" -H \"X-TC-Timestamp: ").append(timestamp).append("\"")
                    .append(" -H \"X-TC-Version: ").append(version).append("\"")
                    .append(" -H \"X-TC-Region: ").append(region).append("\"")
                    .append(" -H \"X-TC-Language: zh-CN\"")
                    .append(" -d '").append(payload).append("'");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
            // 发送请求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity("https://ft.tencentcloudapi.com", requestEntity, String.class);

            // 处理响应
            if (response.getStatusCode().is2xxSuccessful()) {

                String responseBody = response.getBody();
                JSONObject obj = JSONObject.parseObject(responseBody);
                JSONObject data = obj.getJSONObject("Response");
                if (data.get("Error") == null) {
                    data.put("oldImage", base64Image);
                    return Result.success(data);
                } else {
                    return Result.fail(data.getJSONObject("Error").getString("Message"));
                }
            } else {
                System.out.println("Error: " + response.getStatusCodeValue());
                return Result.fail( response.getStatusCodeValue());
            }


        } catch (Exception e) {
            return Result.fail(50000, e.getMessage());
        }
    }

    @ApiOperation(value = "人像动漫化, 传图片地址转化base64")
    @PostMapping("/generative/animation")
    public Result portraitsGenerateAnimeCharacters(@RequestBody String url) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String action = "FaceCartoonPic";
        String host = "ft.tencentcloudapi.com";
        String version = "2020-03-04";
        String service = "ft";
        String region = "ap-nanjing";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 注意时区，否则容易出错
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.valueOf((timestamp + "000"))));
        String algorithm = "TC3-HMAC-SHA256";

        // ************* 步骤 0：图片地址转化base64 *************
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // ************* 步骤 1：拼接规范请求串 *************
        String httpRequestMethod = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json\n"
                + "host:" + host + "\n" + "x-tc-action:" + action.toLowerCase() + "\n";
        String signedHeaders = "content-type;host;x-tc-action";

        String payload = "{\"Image\":\"" + base64Image + "\"}";
        try {
            String hashedRequestPayload = HS256Util.sha256Hex(payload);
            String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                    + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

            // ************* 步骤 2：拼接待签名字符串 *************
            String credentialScope = date + "/" + service + "/" + "tc3_request";
            String hashedCanonicalRequest = HS256Util.sha256Hex(canonicalRequest);
            String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

            // ************* 步骤 3：计算签名 *************
            byte[] secretDate = HS256Util.hmac256(("TC3" + SECRET_KEY).getBytes(HS256Util.UTF8), date);
            byte[] secretService = HS256Util.hmac256(secretDate, service);
            byte[] secretSigning = HS256Util.hmac256(secretService, "tc3_request");
            String signature = HexFormat.of().formatHex(HS256Util.hmac256(secretSigning, stringToSign));

            // ************* 步骤 4：拼接 Authorization *************
            String authorization = algorithm + " " + "Credential=" + SECRET_ID + "/" + credentialScope + ", "
                    + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            headers.set("Content-Type", "application/json");
            headers.set("Host", host);
            headers.set("X-TC-Action", action);
            headers.set("X-TC-Timestamp", timestamp);
            headers.set("X-TC-Version", version);
            headers.set("X-TC-Region", region);
            headers.set("X-TC-Language", "zh-CN");

            StringBuilder sb = new StringBuilder();
            sb.append("curl -X POST https://").append(host)
                    .append(" -H \"Authorization: ").append(authorization).append("\"")
                    .append(" -H \"Content-Type: application/json\"")
                    .append(" -H \"Host: ").append(host).append("\"")
                    .append(" -H \"X-TC-Action: ").append(action).append("\"")
                    .append(" -H \"X-TC-Timestamp: ").append(timestamp).append("\"")
                    .append(" -H \"X-TC-Version: ").append(version).append("\"")
                    .append(" -H \"X-TC-Region: ").append(region).append("\"")
                    .append(" -H \"X-TC-Language: zh-CN\"")
                    .append(" -d '").append(payload).append("'");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity("https://ft.tencentcloudapi.com", requestEntity, String.class);

            // 处理响应
            if (response.getStatusCode().is2xxSuccessful()) {

                String responseBody = response.getBody();
                JSONObject obj = JSONObject.parseObject(responseBody);
                JSONObject data = obj.getJSONObject("Response");
                if (data.get("Error") == null) {
                    data.put("oldImage", url);
                    return Result.success(data);
                } else {
                    return Result.fail(data.getJSONObject("Error").getString("Message"));
                }
            } else {
                System.out.println("Error: " + response.getStatusCodeValue());
                return Result.fail( response.getStatusCodeValue());
            }


        } catch (Exception e) {
            return Result.fail(50000, e.getMessage());
        }
    }

    @ApiOperation(value = "人像年龄变化, 图片地址转化base64")
    @PostMapping("/base64/age/change")
    public Result base64AgeChange(@RequestBody AI ai) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String action = "ChangeAgePic";
        String host = "ft.tencentcloudapi.com";
        String version = "2020-03-04";
        String service = "ft";
        String region = "ap-nanjing";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 注意时区，否则容易出错
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.valueOf((timestamp + "000"))));
        String algorithm = "TC3-HMAC-SHA256";

        // ************* 步骤 0：图片地址转化base64 *************
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(ai.getUrl(), byte[].class);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // ************* 步骤 1：拼接规范请求串 *************
        String httpRequestMethod = "POST";
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json\n"
                + "host:" + host + "\n" + "x-tc-action:" + action.toLowerCase() + "\n";
        String signedHeaders = "content-type;host;x-tc-action";

        String payload = "{\"Image\":\"" + base64Image + "\",\"AgeInfos\":[{\"Age\":" + ai.getAge() + "}],\"RspImgType\":\"url\"}";
        try {
            String hashedRequestPayload = HS256Util.sha256Hex(payload);
            String canonicalRequest = httpRequestMethod + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n"
                    + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;

            // ************* 步骤 2：拼接待签名字符串 *************
            String credentialScope = date + "/" + service + "/" + "tc3_request";
            String hashedCanonicalRequest = HS256Util.sha256Hex(canonicalRequest);
            String stringToSign = algorithm + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

            // ************* 步骤 3：计算签名 *************
            byte[] secretDate = HS256Util.hmac256(("TC3" + SECRET_KEY).getBytes(HS256Util.UTF8), date);
            byte[] secretService = HS256Util.hmac256(secretDate, service);
            byte[] secretSigning = HS256Util.hmac256(secretService, "tc3_request");
            String signature = HexFormat.of().formatHex(HS256Util.hmac256(secretSigning, stringToSign));

            // ************* 步骤 4：拼接 Authorization *************
            String authorization = algorithm + " " + "Credential=" + SECRET_ID + "/" + credentialScope + ", "
                    + "SignedHeaders=" + signedHeaders + ", " + "Signature=" + signature;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            headers.set("Content-Type", "application/json");
            headers.set("Host", host);
            headers.set("X-TC-Action", action);
            headers.set("X-TC-Timestamp", timestamp);
            headers.set("X-TC-Version", version);
            headers.set("X-TC-Region", region);
            headers.set("X-TC-Language", "zh-CN");

            StringBuilder sb = new StringBuilder();
            sb.append("curl -X POST https://").append(host)
                    .append(" -H \"Authorization: ").append(authorization).append("\"")
                    .append(" -H \"Content-Type: application/json\"")
                    .append(" -H \"Host: ").append(host).append("\"")
                    .append(" -H \"X-TC-Action: ").append(action).append("\"")
                    .append(" -H \"X-TC-Timestamp: ").append(timestamp).append("\"")
                    .append(" -H \"X-TC-Version: ").append(version).append("\"")
                    .append(" -H \"X-TC-Region: ").append(region).append("\"")
                    .append(" -H \"X-TC-Language: zh-CN\"")
                    .append(" -d '").append(payload).append("'");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity("https://ft.tencentcloudapi.com", requestEntity, String.class);

            // 处理响应
            if (response.getStatusCode().is2xxSuccessful()) {

                String responseBody = response.getBody();
                JSONObject obj = JSONObject.parseObject(responseBody);
                JSONObject data = obj.getJSONObject("Response");
                if (data.get("Error") == null) {
                    data.put("oldImage", ai.getUrl());
                    return Result.success(data);
                } else {
                    return Result.fail(data.getJSONObject("Error").getString("Message"));
                }
            } else {
                System.out.println("Error: " + response.getStatusCodeValue());
                return Result.fail( response.getStatusCodeValue());
            }


        } catch (Exception e) {
            return Result.fail(50000, e.getMessage());
        }
    }

//    @ApiOperation(value = "图片放大, 图片地址转化base64")
//    @PostMapping("/base64/image/largen")
//    public Result base64ImageLargen(@RequestBody String imageUrl) {
//        return Result.fail("未开发");
//    }

}
