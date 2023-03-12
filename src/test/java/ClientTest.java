import com.iagcargo.ruleengine.controller.RuleEngineControllerApi;
import com.iagcargo.ruleengine.dto.rule.*;
import feign.FeignException;
import org.example.client.ClientProvider;
import org.example.mapping.DateMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class ClientTest {

    private final ClientProvider<RuleEngineControllerApi> provider = new ClientProvider(RuleEngineControllerApi.class);
    private RuleEngineControllerApi feignClient;

    @Before
    public void init() {
        this.feignClient = provider
                .withMapping(Date.class, new DateMapper())
                .provide();
    }

    @Test
    public void shouldLoadPlanById() {
        RulesEnginePlanDto plan;
        try {
            plan = feignClient.loadRulePlanById("plan1");
        } catch (FeignException e) {
            assertEquals(500, e.status());
            return;
        }
        assertEquals("plan1", plan.getId());
        assertEquals(2, plan.getRules().size());
    }

    @Test
    public void shouldUploadNewPlan() {
        //given
        RulesEnginePlanDto plan = preparePlan();
        Date ruleDate = plan.getRules().get(0).getCreatedAt();
        //when
        feignClient.uploadRulePlan(plan);
        //and when
        plan = feignClient.loadRulePlanById(plan.getId());
        //then
        assertThat(plan.getRules().get(0).getCreatedAt(), is(ruleDate));

    }

    @Test
    public void shouldReturnAllRules() {
        List<RulesEnginePlanDto> plans = feignClient.loadAllRulePlans();
        assertEquals(3, plans.size());

    }

    @Test
    public void shouldReturnCurrentRule() {
        RulesEnginePlanDto plan = feignClient.loadCurrentPlan();

        assertEquals("planX", plan.getId());
        assertEquals(1, plan.getRules().size());
    }

    private RulesEnginePlanDto preparePlan() {
        RulesEnginePlanDto plan = new RulesEnginePlanDto();
        plan.setName("New Name");
        plan.setDescription("New plan description");
        plan.setId("planX");
        plan.setRules(Collections.singletonList(prepareRandomRule()));
        return plan;
    }

    private RuleDto prepareRandomRule() {
        RuleDto rule = new RuleDto();
        rule.setId(String.valueOf(Math.floor(Math.random() * 100)));
        rule.setRuleType(RuleType.REQUEST);
        rule.setCreatedAt(new Date());
        rule.setActions(prepareActions());
        return rule;
    }

    private List<ActionDto> prepareActions() {
        ActionDto insertAction = new ActionDto();
        insertAction.setActionType(ActionType.INSERT);
        insertAction.setParams(Arrays.asList("a", "b", "c"));

        ActionDto sortAction = new ActionDto();
        sortAction.setActionType(ActionType.SORT_BY);
        sortAction.setParams(Arrays.asList("d", "e", "f"));

        return Arrays.asList(insertAction, sortAction);
    }
}
