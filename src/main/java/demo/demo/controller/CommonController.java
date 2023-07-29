package demo.demo.controller;

import demo.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
public class CommonController {
    private final FileService fileService;
    @ResponseBody
    @GetMapping("/images/{filename}")
    public UrlResource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFullPath(filename));
    }
}
