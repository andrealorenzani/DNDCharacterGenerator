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

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DiceRoller {
    private Random rand;

    public DiceRoller(){
        this.rand = new Random();
    }
    public DiceRoller(Random rand){
        this.rand = rand;
    }

    public int roll1dN(int ndice) {
        if(ndice < 2) throw new IllegalArgumentException(String.format("Cannot roll a dice with %d faces", ndice));
        return 1+rand.nextInt(ndice);
    }

    public int rollNdM(int ntimes, int mdice){
        if(ntimes == 0) return 0;
        if(ntimes < 0) throw new IllegalArgumentException(String.format("Cannot roll a dice %d times", ntimes));
        return IntStream.rangeClosed(1, ntimes)
                        .map((x) -> roll1dN(mdice))
                        .sum();
    }

    public int maxHitPoint(int dice, int lvl, int mod){
        if(dice<=1-mod) return lvl;
        return Math.max(1, dice+mod)+IntStream.rangeClosed(2, lvl)
                                              .map(x -> Math.max(1, roll1dN(dice)+mod))
                                              .sum();
    }

    public List<Integer> generateCharWith3d6() {
        List<Integer> res = IntStream.rangeClosed(1, 12)
                .map(x -> rollNdM(3, 6))
                .sorted()
                .skip(6)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(res);
        return res;
    }

    public List<Integer> generateCharWith4d6() {
        return IntStream.rangeClosed(1, 6)
                .map(x -> roll4d6best3())
                .boxed()
                .collect(Collectors.toList());
    }

    int roll4d6best3() {
        return IntStream.rangeClosed(1, 4)
                        .map((k) -> roll1dN(6))
                        .sorted()
                        .skip(1)
                        .sum();
    }
}
