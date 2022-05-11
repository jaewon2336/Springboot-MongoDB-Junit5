package site.metacoding.mongocrud.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.metacoding.mongocrud.domain.Naver;
import site.metacoding.mongocrud.domain.NaverRepository;

@RequiredArgsConstructor
@RestController
public class NaverApiController {
    private final NaverRepository naverRepository;

    @GetMapping("/navers")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(naverRepository.findAll(), HttpStatus.OK); // 200
    }

    @PostMapping("/navers")
    public ResponseEntity<?> save(@RequestBody Naver naver) {
        // DB에 save된 데이터를 리턴해주기 때문에 id가 들어가있는 상태
        return new ResponseEntity<>(naverRepository.save(naver), HttpStatus.CREATED); // 201
    }
}
