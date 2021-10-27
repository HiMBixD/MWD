package tch1904.mwd.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tch1904.mwd.controllers.request.UploadFileRequest;
import tch1904.mwd.entity.FileImg;
import tch1904.mwd.entity.dto.FileMusic;
import tch1904.mwd.repository.FileRepository;

import java.util.Optional;

@Service
public class FileServices {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    public String addFileImg(UploadFileRequest request) throws Exception {
        try {
            FileImg fileImg = new FileImg();
            fileImg.setDescription(request.getDescription());
            fileImg.setImage(new Binary(BsonBinarySubType.BINARY, request.getFile().getBytes()));
            fileImg = fileRepository.insert(fileImg);
            return fileImg.getId();
        } catch (Exception e){
            throw e;
        }

    }

    public FileImg getFileImg(String id) {
        return fileRepository.findById(id).get();
    }

    public String addFileMusic(UploadFileRequest request) throws Exception {
        try {
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "music");
            metaData.put("description", request.getDescription());
            ObjectId id = gridFsTemplate.store(request.getFile().getInputStream(), request.getFile().getName(),
                    request.getFile().getContentType(), metaData);
            return id.toString();
        } catch (Exception e){
            throw e;
        }
    }

    public FileMusic getFileMusic(String id) throws Exception {
        try {
            GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
            FileMusic fileMusic = new FileMusic();
            assert file != null;
            assert file.getMetadata() != null;
            fileMusic.setDescription(file.getMetadata().get("title").toString());
            fileMusic.setStream(operations.getResource(file).getInputStream());
            return fileMusic;
        } catch (Exception e) {
            throw e;
        }
    }


}
