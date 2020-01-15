package com.thoughtworks.blockchain.statementloader.loader.mysql;

import org.springframework.batch.item.ItemWriter;

import java.io.OutputStream;
import java.util.List;

public class HttpStreamWriter<T> implements ItemWriter<T> {

    private OutputStream outputStream;

    public HttpStreamWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            outputStream.write(item.toString().getBytes());
            outputStream.flush();
        }
    }
}
