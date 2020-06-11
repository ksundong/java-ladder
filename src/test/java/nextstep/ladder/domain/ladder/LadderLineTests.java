package nextstep.ladder.domain.ladder;

import nextstep.ladder.domain.exceptions.PointsNeedMoreThanOnePersonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LadderLineTests {
    private PointAddStrategy simplePointAddStrategy;

    @BeforeEach
    public void setup() {
        simplePointAddStrategy = new SimplePointAddStrategy();
    }

    @DisplayName("참여하는 인원수를 입력받아서 객체를 생성할 수 있다.")
    @Test
    void createTest() {
        int countOfPerson = 3;
        LadderLine ladderLine = LadderLineFactory.create(countOfPerson, simplePointAddStrategy);
        assertThat(ladderLine).isNotNull();
    }

    @DisplayName("0이하의 인원수로 객체를 생성 할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void createValidationTest(int invalidCountOfPerson) {
        assertThatThrownBy(() -> LadderLineFactory.create(invalidCountOfPerson, simplePointAddStrategy))
                .isInstanceOf(PointsNeedMoreThanOnePersonException.class);
    }

    @DisplayName("첫번째 칸에는 무조건 다리를 놓을 수 없다.")
    @Test
    void firstLadderMustFalseTest() {
        int countOfPerson = 1;
        LadderLine ladderLine = LadderLineFactory.create(countOfPerson, simplePointAddStrategy);
        assertThat(ladderLine.size()).isEqualTo(1);
        assertThat(ladderLine.getPointStatus(0)).isFalse();
    }

    @DisplayName("다리가 없는 칸 다음의 칸에는 다리를 놓을 수 있다.")
    @Test
    void canMakeLadderAfterNoneLadder() {
        int countOfPerson = 2;
        LadderLine ladderLine = LadderLineFactory.create(countOfPerson, simplePointAddStrategy);
        assertThat(ladderLine.size()).isEqualTo(2);
        assertThat(ladderLine.getPointStatus(0)).isFalse();
        assertThat(ladderLine.getPointStatus(1)).isTrue();
    }

    @DisplayName("이전칸에 다리가 놓였다면 이후 칸에는 다리를 놓을 수 없다.")
    @Test
    void cantMakeLadderAfterLadder() {
        int countOfPerson = 3;
        LadderLine ladderLine = LadderLineFactory.create(countOfPerson, simplePointAddStrategy);
        assertThat(ladderLine.size()).isEqualTo(3);
        assertThat(ladderLine.getPointStatus(0)).isFalse();
        assertThat(ladderLine.getPointStatus(1)).isTrue();
        assertThat(ladderLine.getPointStatus(2)).isFalse();
    }

    @DisplayName("정상적인 이동테스트")
    @ParameterizedTest
    @MethodSource
    void normalMoveTest(int countOfPerson, int beforePointIndex, HorizontalMoveStrategy strategy) {
        LadderLine ladderLine = LadderLineFactory.create(countOfPerson, simplePointAddStrategy);

        assertThat(ladderLine.move(beforePointIndex)).isEqualTo(strategy);
    }
    public static Stream<Arguments> normalMoveTest() {
        return Stream.of(
                Arguments.of(3, 1, HorizontalMoveStrategy.MOVE_LEFT),
                Arguments.of(3, 0, HorizontalMoveStrategy.MOVE_RIGHT),
                Arguments.of(3, 2, HorizontalMoveStrategy.STAY),
                Arguments.of(4, 3, HorizontalMoveStrategy.MOVE_LEFT)
        );
    }
}