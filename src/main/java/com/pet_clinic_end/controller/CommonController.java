package com.pet_clinic_end.controller;


import com.pet_clinic_end.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Slf4j
public class CommonController {
    @Value("${pet_clinic.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file)
    {
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;
        File dir = new File(basePath);

        if (!dir.exists())
        {
            dir.mkdirs();
        }

        try{
            file.transferTo(new File(basePath + fileName));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return Result.success("http://106.14.208.53/file/" + fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response)
    {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1)
            {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
