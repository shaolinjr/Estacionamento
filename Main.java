import java.util.*;
public class Main {
	static Scanner leia = new Scanner(System.in);
	static String [] codMarca = {"BM", "VW", "FO","MB","CV", "FI", "AU", "TO", "HO", "HY"};
	static String [] descricaoMarca = {"BMW", "VOLKSWAGEN", "FORD", "MERCEDES BENZ", "CHEVROLET", "FIAT", "AUDI", "TOYOTA", "HONDA", "HYUNDAI"};
	
	
	public static void main(String[] args) {	
		Estacionamento estacionamento = new Estacionamento();
    	byte opcao = -1;
    	 
    	do {
			do {
    			System.out.println("\n ***************  ESTACIONAMENTO  ***************** ");
    			System.out.println(" [1] ENTRADA DE VEÍCULO ");
    			System.out.println(" [2] SAÍDA DE VEÍCULO ");
    			System.out.println(" [3] CONSULTAR VEÍCULO ");
    			System.out.println(" [4] EXCLUIR VEÍCULO ");
    			System.out.println(" [5] RELATÓRIO DE FATURAMENTO ");
    			System.out.println(" [0] SAIR");
    			System.out.print("\nDigite a opção desejada: ");
    			opcao = leia.nextByte();
    			if (opcao < 0 || opcao > 5) {
    				System.out.println("opcao Inválida, digite novamente.\n");
    			}
    		}while (opcao < 0 || opcao > 5);
			leia.nextLine();
			switch (opcao) {
				case 0:
					System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
					break;
				case 1: 					
					estacionamento.entradaVeiculo(); //Inclusão
					break;
				case 2:
					estacionamento.saidaVeiculo(); //Alteração da data de saída e valor pago
					break;
				case 3:
					estacionamento.consultar(); //Consultar veículo pelo código de estacionamento
					break;
				case 4: 
					estacionamento.excluir();
					break;
				case 5: 
					estacionamento.relatorioFaturamento();
					break;
			}
    	} while ( opcao != 0 );
    	leia.close();
	}

}
