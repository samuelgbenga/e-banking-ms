package ng.samuel.notdemo.ebankingms.accountservice.command.utils.generator.impl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, Long> {
}
