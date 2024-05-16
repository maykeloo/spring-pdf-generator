package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.DocumentException;

@Controller
public class PdfController {

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/pdf")
    public void generatePdf(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=example.pdf");

        Map<String, Object> data = new HashMap<>();
        data.put("header", "Header Content");
        data.put("footer", "Footer Content");
        data.put("message", "Hello, Thymeleaf with PDF!");

        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process("pdf_template", context);

        try (OutputStream os = response.getOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);
        }
    }
}