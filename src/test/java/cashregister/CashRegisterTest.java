package cashregister;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CashRegisterTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originOut);
    }


    @Test
    public void should_print_the_real_purchase_when_call_process() {
        //given
        Item item = new Item("A", 12);
        Item item1 = new Item("B", 20);
        Item item2 = new Item("C", 45);
        Item[] items = new Item[] {item, item1, item2};
        Purchase purchase = new Purchase(items);
        Printer printer = new Printer();
        CashRegister cashRegister = new CashRegister(printer);
        //when
        cashRegister.process(purchase);
        //then
        assertThat(outContent.toString()).isEqualTo("A\t12.0\nB\t20.0\nC\t45.0\n");

    }

    @Test
    public void should_print_the_stub_purchase_when_call_process() {
        //given
        Purchase purchase = new Purchase(new Item[]{
                new Item("A", 12),
                new Item("B", 20),
                new Item("C", 45)});
        CashRegister cashRegister = new CashRegister(new Printer(){
            @Override
            public void print(String printThis) {
                System.out.print("stub: " + printThis);
            }
        });
        //when
        cashRegister.process(purchase);
        //then
        assertThat(outContent.toString()).isEqualTo("stub: A\t12.0\nB\t20.0\nC\t45.0\n");
    }

    @Test
    public void should_verify_with_process_call_with_mockito() {
        //given
        Purchase purchase = new Purchase(new Item[]{
                new Item("A", 12),
                new Item("B", 20),
                new Item("C", 45)});
        //when
        Printer mock_printer = mock(Printer.class);
        CashRegister cashRegister = new CashRegister(mock_printer);
        cashRegister.process(purchase);
        //then
        verify(mock_printer).print("A\t12.0\nB\t20.0\nC\t45.0\n");
    }

}
