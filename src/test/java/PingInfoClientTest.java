import com.iagcargo.ruleengine.controller.TestControllerApi;
import com.iagcargo.ruleengine.dto.rule.RulesEnginePlanDto;
import org.example.client.ClientProvider;
import org.example.deserializer.Deserializer;
import org.example.deserializer.InstantDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.threeten.bp.Instant;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PingInfoClientTest {

    private final ClientProvider<TestControllerApi> provider = new ClientProvider(TestControllerApi.class);
    private final Deserializer<Instant> instantDeserializer = new InstantDeserializer();
    private TestControllerApi feignClient;

    @Before
    public void init() {
        this.feignClient = provider
                .withMapping(Instant.class, instantDeserializer)
                .provide();
    }

    @Test
    public void givenBookClient_shouldRunSuccessfully() {
        List<RulesEnginePlanDto> plans = feignClient.loadAll();
        assertEquals(2, plans.size());
    }
}
