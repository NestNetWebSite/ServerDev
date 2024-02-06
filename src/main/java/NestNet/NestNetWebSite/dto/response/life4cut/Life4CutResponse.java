package NestNet.NestNetWebSite.dto.response.life4cut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Life4CutResponse {

    private List<Life4CutDto> dtoList = new ArrayList<>();
}
