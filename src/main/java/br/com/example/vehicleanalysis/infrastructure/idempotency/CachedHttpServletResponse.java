package br.com.example.vehicleanalysis.infrastructure.idempotency;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class CachedHttpServletResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final ServletOutputStream out = new ServletOutputStream() {
        @Override
        public boolean isReady() { return true; }
        @Override
        public void setWriteListener(WriteListener writeListener) {}
        @Override
        public void write(int b) throws IOException { buffer.write(b); }
    };

    private PrintWriter writer;

    public CachedHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException { return out; }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) writer = new PrintWriter(buffer, true, StandardCharsets.UTF_8);
        return writer;
    }

    public byte[] getBody() { return buffer.toByteArray(); }
}
