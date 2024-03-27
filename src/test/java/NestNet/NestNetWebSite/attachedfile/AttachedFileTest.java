package NestNet.NestNetWebSite.attachedfile;

import NestNet.NestNetWebSite.repository.attachedfile.AttachedFileRepository;
import NestNet.NestNetWebSite.service.attachedfile.AttachedFileService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
public class AttachedFileTest {

    @InjectMocks
    private AttachedFileService attachedFileService;

    @Mock
    AttachedFileRepository attachedFileRepository;
}
