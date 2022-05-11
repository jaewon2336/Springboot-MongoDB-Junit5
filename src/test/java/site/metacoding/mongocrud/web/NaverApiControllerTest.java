package site.metacoding.mongocrud.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import site.metacoding.mongocrud.domain.Naver;

// @RequiredArgsConstructor // 사용못함, 테스트할때는 @Autowired 사용하자
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // 통합테스트 -> 이 파일이 실행될 때 모든게 메모리에 다 뜸 -> 시간 좀 걸림
public class NaverApiControllerTest {

    @Autowired // DI 어노테이션
    private TestRestTemplate rt; // http 통신 -> 컨트롤러 때리기
    private static HttpHeaders headers;

    @BeforeAll // 이 파일이 실행되기 직전 최초에 실행 -> static 붙어야함 (통합테스트니까 하나씩 실행안할거야!)
    public static void init() {
        // assertNotNull(rt); // rt가 null이 아니면 true
        headers = new HttpHeaders(); // 재사용하기 위해 init에 생성
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void save_테스트() throws JsonProcessingException { // 메서드 전체 트라이캐치(캐치 처리안됨, 호출한 쪽에서 캐치 처리해야함)
        // given (가짜 데이터 만들기)
        Naver naver = new Naver(); // json으로 바꿔서 전송
        naver.setTitle("스프링1강");
        naver.setCompany("재밌어요");

        ObjectMapper om = new ObjectMapper(); // 바이트도 오브젝트로 바꿔줌. Gson 보다 많은 기능을 제공함.
        String content = om.writeValueAsString(naver); // 오브젝트 -> json 변환

        // 헤더도 넣어줘야하고 하니까 객체를 만들어서 content를 담아 전송해야함
        HttpEntity<String> httpEntity = new HttpEntity<>(content, headers);

        // when (실행)
        ResponseEntity<String> response = rt.exchange("/navers", HttpMethod.POST, httpEntity, String.class);

        // then (검증)
        // 눈으로 확인하는 검증은 확실하지 않을 수 있다.
        // System.out.println("======================================================");
        // System.out.println(response.getBody());
        // System.out.println(response.getHeaders());
        // System.out.println(response.getStatusCode());
        // System.out.println(response.getStatusCode().is2xxSuccessful()); // true
        // System.out.println("======================================================");
        // assertTrue(response.getStatusCode().is2xxSuccessful()); // true면 초록색 뜨고 성공

        // 키값을 찾아서 문자열 비교 검증. 다시 json을 오브젝트로 변환하기 귀찮으니까 이거 사용
        DocumentContext dc = JsonPath.parse(response.getBody());
        // System.out.println(dc.jsonString());

        // 키값 찾는 방법 junit5 jsonpath 문법
        String title = dc.read("$.title");
        // System.out.println(title);

        assertEquals("스프링1강", title);
    }

    @Test
    public void findAll_테스트() {
        // given (SELECT라서 줄 데이터가 없다)

        // when (실행)
        ResponseEntity<String> response = rt.exchange("/navers", HttpMethod.GET, null, String.class);

        // then

        // DocumentContext dc = JsonPath.parse(response.getBody());
        // String title = dc.read("$.[0].title");
        // findAll은 상태코드 확인해주자. 배포했을 때 그쪽에 데이터 없을수도 있으니까!
        // assertEquals("지방선거 6.1이 곧 다가온다.", title);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}