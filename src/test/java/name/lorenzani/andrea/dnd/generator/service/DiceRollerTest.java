/***
*   Copyright 2018 Andrea Lorenzani
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*
***/

package name.lorenzani.andrea.dnd.generator.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.when;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
public class DiceRollerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    public Random randMock;

    public DiceRoller sut;
    private AtomicInteger value;

    @Before
    public void setUp() throws Exception {
        value = new AtomicInteger(0);
        when(randMock.nextInt(ArgumentMatchers.anyInt())).thenReturn(0, 1, 2, 3, 4, 5, 6, 7);
        sut = new DiceRoller(randMock);
    }

    @Test
    public void shouldRollAD6(){
        int res = sut.roll1dN(6);
        assertEquals(1, res);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailRollingAD1(){
        sut.roll1dN(1);
        fail("Should have thrown exception");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailRollingNegativeTimesAD6(){
        sut.rollNdM(-1, 6);
        fail("Should have thrown exception");
    }

    @Test
    public void shouldRoll0D6And3d6(){
        assertEquals(0, sut.rollNdM(0, 7));
        int res = sut.rollNdM(3, 6);
        assertEquals(6, res);
    }

    @Test
    public void shouldRoll4D6Best3(){
        assertEquals(9, sut.roll4d6best3());
        assertEquals(21, sut.roll4d6best3());
    }

    @Test
    public void shouldGiveBasicHitPoint(){
        assertEquals(9, sut.maxHitPoint(96, 9, -100));
        assertEquals(40, sut.maxHitPoint(4, 9, 0));
    }

    @Test
    public void shouldGiveHitPointWithModif(){
        assertEquals(67, sut.maxHitPoint(4, 9, 3));
    }

    @Test
    public void shouldGiveHitPointWithNegModif(){
        // 2 1 1 1 1 2 3 4 5
        assertEquals(20, sut.maxHitPoint(5, 9, -3));
    }
}
