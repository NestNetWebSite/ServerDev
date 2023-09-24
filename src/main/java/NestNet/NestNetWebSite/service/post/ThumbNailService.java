package NestNet.NestNetWebSite.service.post;

import NestNet.NestNetWebSite.api.ApiResult;
import NestNet.NestNetWebSite.domain.attachedfile.AttachedFile;
import NestNet.NestNetWebSite.domain.post.Post;
import NestNet.NestNetWebSite.domain.post.photo.ThumbNail;
import NestNet.NestNetWebSite.dto.response.ThumbNailResponse;
import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.repository.post.ThumbNailRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ThumbNailService {

    private final ThumbNailRepository thumbNailRepository;
    private final EntityManager entityManager;

    // Post 자식 객체를 가져오기 위한 간단한 로직 수행
    public Post findPost(Long postId){
        return entityManager.find(Post.class, postId);
    }

    /*
    첨부파일 저장 -> 저장 로직은 게시물에 종속적. 게시물 저장하는 서비스 로직에서 수행
     */

    /*
    게시판 화면에서 썸네일 모두 조회
     */
//    public ByteArrayResource findAllThumbNail(int offset, int limit){
//
//        List<ThumbNail> thumbNailList = thumbNailRepository.findAllPhotoThumbNailByPaging(offset, limit);
//
//        Map<ByteArrayResource, List<Map<String, Object>>> result = new HashMap<>();
//        List<Map<String, Object>> postData = new ArrayList<>();
//        List<byte[]> byteFileList = new ArrayList<>();
//        try{
//            for(ThumbNail thumbNail : thumbNailList){
//
////                File realFile = new File(thumbNail.getSaveFilePath() + File.separator + thumbNail.getSaveFileName());
//
//                Path path = Paths.get(thumbNail.getSaveFilePath() + File.separator + thumbNail.getSaveFileName());
//
//                byteFileList.add(Files.readAllBytes(path));     //하나의 파일을 byte로 읽어서 리스트에 저장
//
//                Map<String, Object> data = new HashMap<>();         //썸네일 사진의 정보
//                data.put("postId", thumbNail.getPost().getId());
//                data.put("title", thumbNail.getPost().getTitle());
//                data.put("viewCount", thumbNail.getPost().getViewCount());
//                data.put("likeCount", thumbNail.getPost().getLikeCount());
//
//                postData.add(data);
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        ByteArrayResource byteArrayResource = new ByteArrayResource(mergeFilessToByte(byteFileList));
//
//        return byteArrayResource;
//    }
    public void findAllThumbNail(int offset, int limit, HttpServletResponse response){

        List<ThumbNail> thumbNailList = thumbNailRepository.findAllPhotoThumbNailByPaging(offset, limit);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        try{
            for(ThumbNail thumbNail : thumbNailList){

                Path path = Paths.get(thumbNail.getSaveFilePath() + File.separator + thumbNail.getSaveFileName());

                ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));  //파일 하나를 byte로 읽어옴

                OutputStream outputStream = response.getOutputStream();

                byte[] buffer = new byte[1024];
                int byteRead;
                while((byteRead = byteArrayResource.getInputStream().read(buffer)) != -1){      //-1이면 읽어온게 없는 것
                    outputStream.write(buffer, 0, byteRead);        //0부터 읽어온 길이만 큼 write
                }
                outputStream.flush();
                outputStream.close();
            }

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    // byte 파일들을 하나의 byte 파일로 병합
    private byte[] mergeFilessToByte(List<byte[]> byteFileList){

        ByteArrayOutputStream mergedStream = new ByteArrayOutputStream();

        for(byte[] data : byteFileList){
            try{
                mergedStream.write(data);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return mergedStream.toByteArray();
    }


}
