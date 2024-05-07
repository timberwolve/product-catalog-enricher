package com.bw.pce.converter;

import com.bw.pce.utils.ListParam;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class CsvHttpMessageConverter<T, L extends ListParam<T>>
        extends AbstractHttpMessageConverter<L> {

    public CsvHttpMessageConverter () {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports (Class<?> clazz) {
        return ListParam.class.isAssignableFrom(clazz);
    }

    @Override
    protected L readInternal (Class<? extends L> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
//        //HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
//        Class<T> t = toBeanType(clazz.getGenericSuperclass());
//        //strategy.setType(t);
//        StringTokenizer tradesTokenizer = new StringTokenizer(new BufferedReader(
//                new InputStreamReader(inputMessage.getBody(), StandardCharsets.UTF_8))
//                .lines()
//                .collect(Collectors.joining("\n")), "\n");
//
//        CSVReader csv = new CSVReaderBuilder(new InputStreamReader(inputMessage.getBody())).build();
//        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csv).
//                                    withType(t).
//                                    build();
//        List<T> beanList = csvToBean.parse();
//        try {
//            L l = clazz.newInstance();
//            l.setList(beanList);
//            return l;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void writeInternal (L l, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
//
//        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
//        strategy.setType(toBeanType(l.getClass().getGenericSuperclass()));
//
//        OutputStreamWriter outputStream = new OutputStreamWriter(outputMessage.getBody());
//        StatefulBeanToCsv<T> beanToCsv =
//                new StatefulBeanToCsvBuilder(outputStream)
//                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
//                        .withMappingStrategy(strategy)
//                        .build();
//        try {
//            beanToCsv.write(l.getList());
//            outputStream.close();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @SuppressWarnings("unchecked")
    private Class<T> toBeanType (Type type) {
        return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
    }
}
