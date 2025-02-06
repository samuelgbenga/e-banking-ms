package ng.samuel.notdemo.ebankingms.accountservice.command.utils.generator.impl;


import ng.samuel.notdemo.ebankingms.accountservice.command.utils.generator.IdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class IdGeneratorImpl implements IdGenerator {

    private final CounterRepository counterRepository;

    private final Random random = new Random();

    public IdGeneratorImpl(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Transactional
    @Override
    public String autoGenerateId() {
        Long id = getNewCounterId();  // Get sequential ID
        String idStr = id.toString();

        int missingDigits = 10 - idStr.length();

        // Generate a random number with 'missingDigits' length
        int randomPart = random.nextInt((int) Math.pow(10, missingDigits));

        // Format randomPart to ensure leading zeros when necessary
        String randomPadding = String.format("%0" + missingDigits + "d", randomPart);

        return randomPadding + idStr;
    }

    private Long getNewCounterId(){
        Counter savedCounter =  counterRepository.save(new Counter());
        return savedCounter.getId();
    }
}
