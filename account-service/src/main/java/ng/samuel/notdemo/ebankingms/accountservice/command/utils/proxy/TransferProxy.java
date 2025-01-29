package ng.samuel.notdemo.ebankingms.accountservice.command.utils.proxy;

import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.accountservice.command.commands.CreditAccountCommand;
import ng.samuel.notdemo.ebankingms.accountservice.command.commands.DebitAccountCommand;
import ng.samuel.notdemo.ebankingms.accountservice.command.dto.TransferRequestDTO;
import ng.samuel.notdemo.ebankingms.accountservice.command.utils.factory.CommandFactory;
import ng.samuel.notdemo.ebankingms.accountservice.common.security.SecurityInformation;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
public class TransferProxy {

    public TransferProxy(){
        super();
    }


    //Responsible for transfer and reversing a transfer in case of any issue  of any issue
    // use async with join to complete debiting before moving to crediting and completing crediting before moving
    // to reverse crediting
    public List<CompletableFuture<String>> transfer(final TransferRequestDTO dto, @NotNull CommandGateway commandGateway, @NotNull SecurityInformation securityInformation){
        log.info("Transfer request: {}", dto);

        DebitAccountCommand debitCommand = CommandFactory.createDebitAccountCommand(dto, securityInformation.getUsername());
        CompletableFuture<String> debited = commandGateway.send(debitCommand);
        debited.join();
        log.info("Debited success: {}", debited);

        try{
            CreditAccountCommand creditCommand = CommandFactory.createCreditAccountCommand(dto, securityInformation.getUsername());
            CompletableFuture<String> credited = commandGateway.send(creditCommand);
            credited.join();
            log.info("Credited success: {}", credited);
            return List.of(debited, credited);
        }catch (Exception e){
            log.warn(e.getMessage());
            CreditAccountCommand creditCommand = CommandFactory.createCreditAccountCommandReverse(dto, securityInformation.getUsername());
            CompletableFuture<String> credited = commandGateway.send(creditCommand);
            credited.join();
            log.info("System compensation: {}", credited);
            return List.of(debited, credited);
        }
    }
}
