package com.pet_clinic_end.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet_clinic_end.common.Result;
import com.pet_clinic_end.entity.Case;
import com.pet_clinic_end.entity.CaseDetail;
import com.pet_clinic_end.entity.Item;
import com.pet_clinic_end.entity.User;
import com.pet_clinic_end.service.CaseService;
import com.pet_clinic_end.service.FileService;
import com.pet_clinic_end.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Slf4j
public class CommonController {
    @Value("${pet_clinic.path}")
    private String basePath;

    @Autowired
    FileService fileService;

    @Autowired
    UserService userService;
    @Autowired
    CaseService caseService;
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
        com.pet_clinic_end.entity.File file1 = new com.pet_clinic_end.entity.File();
        file1.setUrl("http://106.14.208.53/file/" + fileName);
        fileService.save(file1);

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

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize)
    {
        log.info(page + " " + pageSize);
        Page<com.pet_clinic_end.entity.File> filePage = new Page<>(page, pageSize);
        LambdaQueryWrapper<com.pet_clinic_end.entity.File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc();
        fileService.page(filePage, queryWrapper);

//        Page<com.pet_clinic_end.entity.File> comFilePage = new Page<>();
//        List<com.pet_clinic_end.entity.File> list = filePage.getRecords();
//        for (com.pet_clinic_end.entity.File now : list)
//        {
//            String ori = now.getUrl();
//            now.setUrl("http://106.14.208.53/file/" + ori);
//        }
//
//        comFilePage.setRecords(list);
//        return Result.success(comFilePage);
          return Result.success(filePage);
    }

    @GetMapping("/delete")
    public Result<String> delete(String name)
    {
        try {
            String FileUrl = name;
            LambdaQueryWrapper<com.pet_clinic_end.entity.File> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(name != null, com.pet_clinic_end.entity.File::getUrl, FileUrl);
            com.pet_clinic_end.entity.File MyFile = fileService.getOne(lambdaQueryWrapper);

            if (MyFile == null)
            {
                return Result.error("找不到表中文件，删除失败");
            }

            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(name != null, User::getImage, FileUrl);
            User user = userService.getOne(userLambdaQueryWrapper);

            if (user != null)
            {
                return Result.error("图片被用作头像，删除失败");
            }

            List<CaseDetail> caseDetailList = caseService.queryCaseDetail(FileUrl);
            if (caseDetailList.size() != 0)
            {
                return Result.error("图片被用作病例数据，删除失败");
            }

            fileService.remove(lambdaQueryWrapper);

            String pureName = name.replace("http://106.14.208.53/file/", "");
            File file = new File(basePath + pureName);
            if (file.exists())
            {
                file.delete();
                System.out.println("===============删除成功==============");
                return Result.success("删除成功");
            }
            else
            {
                System.out.println("===============删除失败==============");
                return Result.error("找不到实际文件，删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("出现未知错误，删除失败");
        }
    }
}
