package com.henu.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.henu.commom.Result;
import com.henu.controller.request.BookPageRequest;
import com.henu.controller.request.CategoryPageRequest;
import com.henu.entity.Admin;
import com.henu.entity.Book;
import com.henu.entity.Category;
import com.henu.service.BookService;
import com.henu.service.CategoryService;
import com.henu.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    private static final String BASE_FILE_PATH = System.getProperty("user.dir") + "/files/";
    @GetMapping("/list")
    public Result list(){
        List<Book> books = bookService.list();
        return Result.success(books);
    }

    @PostMapping("/file/upload")
    public Result uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            return Result.error("文件上传失败");
        }
        long flag = System.currentTimeMillis();
        String filePath = BASE_FILE_PATH + flag + "_" + originalFilename;
        try {
            FileUtil.mkParentDirs(filePath);  // 创建父级目录
            file.transferTo(FileUtil.file(filePath));
            Admin currentAdmin = TokenUtils.getCurrentAdmin();
            String token = TokenUtils.genToken(currentAdmin.getId().toString(), currentAdmin.getPassword(), 15);
            String url = "http://localhost:9090/api/book/file/download/" + flag + "?&token=" + token;
            if (originalFilename.endsWith("png") || originalFilename.endsWith("jpg") || originalFilename.endsWith("pdf")) {
                url += "&play=1";
            }
            return Result.success(url);
        } catch (Exception e) {
            log.info("文件上传失败", e);
        }
        return Result.error("文件上传失败");
    }
    @GetMapping("/file/download/{flag}")
    public void download(@PathVariable String flag, @RequestParam(required = false) String play, HttpServletResponse response) {
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(BASE_FILE_PATH);
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse(""); //  System.currentTimeMillis() + originalFilename
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                String realName = fileName.substring(fileName.indexOf("_") + 1);
                if ("1".equals(play)) {
                    response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(realName, "UTF-8"));
                } else {
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
                }
                byte[] bytes = FileUtil.readBytes(BASE_FILE_PATH + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.error("文件下载失败", e);
        }
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        Book book = bookService.getById(id);
        return Result.success(book);
    }

    @GetMapping("/page")
    public Result page(BookPageRequest bookPageRequest){
        return Result.success(bookService.page(bookPageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody Book book){
        bookService.save(book);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody  Book book){
        bookService.update(book);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        bookService.deleteById(id);
        return Result.success();
    }

}
