package com.example.literatura.Service;

public interface IConvertData {
    <T> T getData(String json, Class<T> tClass);
}
