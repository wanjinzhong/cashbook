package com.neil.cashbook.controller;
import com.neil.cashbook.auth.AuthRequired;
import com.neil.cashbook.service.CosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("public/api")
@AuthRequired
public class CosTest {

    @Autowired
    private CosService cosService;

    @PostMapping("upload")
    public void upload(MultipartFile file) {
        cosService.putObject(file, "test");
    }

    @DeleteMapping("delete")
    public void delete(String key) {
        cosService.deleteObject(key);
    }
}
