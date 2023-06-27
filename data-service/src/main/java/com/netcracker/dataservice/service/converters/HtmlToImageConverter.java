package com.netcracker.dataservice.service.converters;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class HtmlToImageConverter {

    public void convertToImage(String html, String outputPath) throws IOException, InterruptedException {
        File tempFile = (File.createTempFile("template", ".html"));
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8))){
            bw.write(html);
        }
        Process wkhtml; // Create uninitialized process
        String command = String.format("wkhtmltoimage --quality 10 %s %s ",tempFile.getAbsolutePath(),outputPath); // Desired command
        wkhtml = Runtime.getRuntime().exec(command); // Start process
        IOUtils.copy(wkhtml.getErrorStream(), System.err); // Print output to console
        wkhtml.waitFor(); // Allow process to run

    }
}
