import java.io.*;
import java.util.*;

import javax.management.Descriptor;

public class Estacionamento {
	static char			ativo;
	static String		codEst;
	static String		placa;
	static char			tipoOperacao;
	static String		codMarca;
	static String		modeloCor;
	static String		categoriaVeiculo;
	static String		dataOperacao;
	static String		horaEntrada;
	static String		horaSaida;
	static float		valorPago;
	boolean placaValida; //Variável nova
	int marcaValida; //Variável nova
	char confirmacao; //Variável nova
	String codEstChave; //Variavel nova
	long posicaoRegistro = 0; //Variavel nova


	static RandomAccessFile arquivo;

	/******************** MÉTODOS PRINCIPAIS ********************/


	public void saidaVeiculo() { //Alteração
		
		do {
    		System.out.println(" ***************  SAÍDA DE VEÍCULOS  ***************** ");
    		do {
    			//Main.leia.nextLine();
    			System.out.print("Digite o código do veÌculo que deseja dar saída ( FIM para encerrar ): ");
    			codEstChave = Main.leia.nextLine();
    			if (codEstChave.equals("FIM")) {
    				break;
    			}
    			posicaoRegistro = pesquisarVeiculoPorCodigo(codEstChave);
    			Main.leia.nextLine();
   				if (posicaoRegistro == -1) {
   					System.out.println("VeÌculo não cadastrado no arquivo, digite outro código\n");
   				}
   				
    		}while (posicaoRegistro == -1);

    		if (codEstChave.equals("FIM")) {
    			System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
    			break;
    		}
    		
    		System.out.println("Placa..........: " + placa);
    		System.out.println("Marca..........: " + codMarca);
    		System.out.println("Cor............: " + modeloCor);
    		System.out.println("Categoria......: " + categoriaVeiculo);
    		System.out.println("Hora de entrada: " + horaEntrada);
    		System.out.println();
    		
    		System.out.print("Digite a data de operação......................:");
    		dataOperacao = Main.leia.next();

    		boolean horaValida;
    		do{
    			System.out.print("Digite a hora de saída.......................:");
    			horaSaida = Main.leia.next();
    			horaValida = consistirHora(horaSaida);
    			if (! horaValida){
    				System.out.println("Hora inválida. ");
    			}
    		}while(! horaValida);
    		
    		valorPago = calcularValorPago(horaSaida);
    		System.out.print("Valor a pagar: " + valorPago);
    		do {
    			System.out.print("\nConfirma a saída e pagamento do veículo (S/N) ? ");
    			confirmacao = Main.leia.next().charAt(0);
    			if (confirmacao == 'S') {
    				salvarVeiculo();
    			}
    		}while (confirmacao != 'S' && confirmacao != 'N');
    		
		}while(codEstChave.equals("FIM"));
	}


	public void excluir(){
   	 
    	do {
    		System.out.println(" ***************  EXCLUSÃO DE VEÍCULOS  ***************** ");
    		do {
    			//Main.leia.nextLine();
    			System.out.print("Digite o código do veÌculo que deseja excluir ( FIM para encerrar ): ");
    			codEstChave = Main.leia.nextLine();
    			if (codEstChave.equals("FIM")) {
    				break;
    			}
    			posicaoRegistro = pesquisarVeiculoPorCodigo(codEstChave);
    			Main.leia.nextLine();
   				if (posicaoRegistro == -1) {
   					System.out.println("VeÌculo não cadastrado no arquivo, digite outro código\n");
   				}
   				
    		}while (posicaoRegistro == -1);

    		if (codEstChave.equals("FIM")) {
    			System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
    			break;
    		}

    		System.out.println("Placa.......: " + placa);
    		System.out.println("Marca.......: " + codMarca);
    		System.out.println("Cor.........: " + modeloCor);
    		System.out.println("Categoria...: " + categoriaVeiculo);
    		System.out.println();
    		
	    	do {
	    		System.out.print("\nConfirma a exclusão deste veículo (S/N) ? ");
	    		confirmacao = Main.leia.next().charAt(0);
	    		if (confirmacao == 'S') {
	    			desativarVeiculoEstacionamento(posicaoRegistro);
	    			System.out.print("Veículo excluído.");
	    		 }
	    	}while (confirmacao != 'S' && confirmacao != 'N');

    	}while ( ! codEstChave.equals("FIM"));
	}

	public void relatorioFaturamento (){

	}

	// Metodo de consulta de veículos

	/*
	 *O programa de consulta deverá apresentar na tela os dados dos veículos com as seguintes opções:
		1 - Exibir todos os registros
		2 - Exibir somente os veículos que ainda não saíram do estacionamento
		3 - Exibir os registros cadastrados em uma Data informada

	Exemplo de layout da consulta:

	Placa          OP  Desc. veículo         Desc.Marca      Categoria     Data            Hr Entr    Hr Saída     Vlr Pago
	------------  --- ---------------------  --------------  ------------  --------------  ---------  -----------  ----------
	ABC1111   		E   Palio cinza             Fiat	      PN	         01/03/2014     10:20	
	XYZ2222   		S   Gol vermelho           Volkswagen     PN	         12/02/2014     12:15       13:15       7,00	

	 */
	public static void consultar (){
		
		byte escolha = -1;
		boolean encontrouAlgumRegistro = false;
		
		System.out.println("****************** PROGRAM DE CONSULTA ******************");

		do {
			System.out.println("Escolha a opção que deseja realizar, digite 0 para sair.");
			System.out.println("\n[ 1 ] Exibir todos os registros");
			System.out.println("[ 2 ] Exibir somente os veículos que ainda não saíram do estacionamento");
			System.out.println("[ 3 ] Exibir somente os veículos cadastrados em uma data específica");
			System.out.print("\nO que deseja fazer?: ");
			escolha = Main.leia.nextByte();

			if (escolha < 0){
				System.out.println("Opção Inválida. Digite novamente.");
			}else if (escolha == 0){
				System.out.println("Até mais...");
				break;
			}
		}while(escolha < 0);

		switch (escolha){
			case 1:
				try{ 
					arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
					//arquivo.seek(0); // garantir que esteja na posicao 0 na primeira rodada
					relatorioHeader();
					while (true){
	
						ativo		 = arquivo.readChar();
						codEst       = arquivo.readUTF();
						placa        = arquivo.readUTF();
						tipoOperacao = arquivo.readChar();
						codMarca     = arquivo.readUTF();
						modeloCor    = arquivo.readUTF();
						categoriaVeiculo = arquivo.readUTF();
						dataOperacao = arquivo.readUTF();
						horaEntrada  = arquivo.readUTF();
						horaSaida    = arquivo.readUTF();
						valorPago    = arquivo.readFloat();
	
						if (ativo == 'S'){
							encontrouAlgumRegistro = true;
							
							if (horaSaida.equals("")){
								horaSaida = "    -";
							}
						
							System.out.println(placa+" " + tipoOperacao +"  " +" "+ 
							modeloCor +"\t     "+ gerarDescMarca(codMarca) +"\t  " +categoriaVeiculo +"\t"+ 
							dataOperacao +"     "+ horaEntrada +"     "+ horaSaida +"\t\t"+ valorPago);
						}
					}
	
					
	
				}catch (EOFException e){
					if (!encontrouAlgumRegistro){
						System.out.println("Não foi encontrado nenhum registro.");
					}
				}catch (IOException e){
					System.out.println("Ocorreu um erro ao tentar abrir o arquivo. Finalizando o programa.");
					System.exit(0);
				}
				
				break;
			case 2:
				try{ 
					arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
					arquivo.seek(0); // garantir que esteja na posicao 0 na primeira rodada
					relatorioHeader();
					
					while (true){
	
						ativo		 = arquivo.readChar();
						codEst       = arquivo.readUTF();
						placa        = arquivo.readUTF();
						tipoOperacao = arquivo.readChar();
						codMarca     = arquivo.readUTF();
						modeloCor    = arquivo.readUTF();
						categoriaVeiculo = arquivo.readUTF();
						dataOperacao = arquivo.readUTF();
						horaEntrada  = arquivo.readUTF();
						horaSaida    = arquivo.readUTF();
						valorPago    = arquivo.readFloat();
	
						if (ativo == 'S' && horaSaida.equals("")){
							encontrouAlgumRegistro = true;
							if (horaSaida.equals("")){
								horaSaida = "    -";
							}
							
							
							System.out.println(placa+" " + tipoOperacao +"  " +" "+ 
									modeloCor +"\t     "+ gerarDescMarca(codMarca) +"\t  " +categoriaVeiculo +"\t"+ 
									dataOperacao +"     "+ horaEntrada +"     "+ horaSaida +"\t\t"+ valorPago);
	
						}
	
						
					}
	
				}catch (EOFException e){
					if (!encontrouAlgumRegistro){
						System.out.println("Não foi encontrado nenhum registro.");
					}
				}catch (IOException e){
					System.out.println("Ocorreu um erro ao tentar abrir o arquivo. Finalizando o programa.");
					System.exit(0);
				}
				break;
			case 3:
	
				String data = "";
	
	
				System.out.print("Digite a data para gerar relatório: ");
				data = Main.leia.next();
				if (!consistirData(data)){
					System.out.println("Data inválida! Formato: dd/mm/yyy");
					break;
				}
	
				try{ 
					arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
					arquivo.seek(0); // garantir que esteja na posicao 0 na primeira rodada
					relatorioHeader();
					while (true){
	
						ativo		  = arquivo.readChar();
						codEst       = arquivo.readUTF();
						placa        = arquivo.readUTF();
						tipoOperacao = arquivo.readChar();
						codMarca     = arquivo.readUTF();
						modeloCor    = arquivo.readUTF();
						categoriaVeiculo = arquivo.readUTF();
						dataOperacao = arquivo.readUTF();
						horaEntrada  = arquivo.readUTF();
						horaSaida    = arquivo.readUTF();
						valorPago    = arquivo.readFloat();
	
						if (ativo == 'S' && dataOperacao.equals(data)){
							encontrouAlgumRegistro = true;
							if (horaSaida.equals("")){
								horaSaida = "    -";
							}
							
							System.out.println(placa+" " + tipoOperacao +"  " +" "+ 
									modeloCor +"\t     "+ gerarDescMarca(codMarca) +"\t  " +categoriaVeiculo +"\t"+ 
									dataOperacao +"     "+ horaEntrada +"     "+ horaSaida +"\t\t"+ valorPago);
	
						}
	
						
					}

			}catch (EOFException e){
				if (!encontrouAlgumRegistro){
					System.out.println("Não foi encontrado nenhum registro.");
				}
			}catch (IOException e){
				System.out.println("Ocorreu um erro ao tentar abrir o arquivo. Finalizando o programa.");
				System.exit(0);
			}
			break;
		}



	}

	

	public void entradaVeiculo() { //Inclusão
		//gerar código sequencial
		String maiorCodEst = "000000";
		try{
			RandomAccessFile arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
			while (true){
				ativo		 = arquivo.readChar();
				codEst       = arquivo.readUTF();
				placa        = arquivo.readUTF();
				tipoOperacao = arquivo.readChar();
				codMarca     = arquivo.readUTF();
				modeloCor    = arquivo.readUTF();
				categoriaVeiculo = arquivo.readUTF();
				dataOperacao = arquivo.readUTF();
				horaEntrada  = arquivo.readUTF();
				horaSaida    = arquivo.readUTF();
				valorPago    = arquivo.readFloat();
				
				if (Integer.parseInt(codEst) > Integer.parseInt(maiorCodEst)){
					maiorCodEst = codEst;
				}
			}
		}catch(EOFException e){
			codEst = String.valueOf( Integer.parseInt(maiorCodEst) + 1 );
			while (codEst.length() < 6){
				codEst = "0" + codEst;
			}
		}catch (IOException e) { 
			System.out.println("Erro na abertura do arquivo  -  programa será finalizado");
			System.exit(0);
		}
		System.out.println("Código de Estacionamento: " + codEst);
		ativo = 'S';

		do {
			System.out.print("Digite a placa do carro.............: ");
			placa = Main.leia.next();
			placaValida = consistirPlaca(placa);
			if (! placaValida){
				System.out.println("A placa digitada é inválida.");
			}
		} while(! placaValida);

		tipoOperacao = 'E';
		System.out.println("Tipo de operação......................: E - Entrada");

		System.out.println("Marcas: BM, VW, FO,MB,CV, FI, AU, TO, HO, HY");
		do {
			System.out.println("Digite o código da marca..............:");
			codMarca = Main.leia.next();
			marcaValida = pesquisarMarcaVeiculo(codMarca);
			if (marcaValida < 0){
				System.out.println("Marca inválida. ");
			}
		} while(marcaValida < 0);

		Main.leia.nextLine();

		do {
			System.out.print("Digite o modelo/cor......................:");
			modeloCor = Main.leia.nextLine();
			if(modeloCor.length()<10){
				System.out.println("O campo deverá ter no mínimo 10 caracteres.");
			}
		}while(modeloCor.length()<10);

		do {
			System.out.println("Categorias: "
					+ "\nGI – Grande e Importado"
					+ "\nPI – Pequeno e Importado"
					+ "\nGN – Grande e Nacional"
					+ "\nPN – Pequeno e Nacional");
			System.out.print("Digite a categoria.........................:");
			categoriaVeiculo = Main.leia.nextLine();
			System.out.println(consistirCategoria(categoriaVeiculo));
			if (consistirCategoria(categoriaVeiculo).equalsIgnoreCase("ERRO")){
				System.out.println("Categoria inválida.");
			}
		} while (consistirCategoria(categoriaVeiculo).equalsIgnoreCase("ERRO"));

		System.out.print("Digite a data de operação......................:");
		dataOperacao = Main.leia.next();

		boolean horaValida;
		do{
			System.out.print("Digite a hora de entrada.......................:");
			horaEntrada = Main.leia.next();
			horaValida = consistirHora(horaEntrada);
			
			if (! horaValida){
				System.out.println("Hora inválida. ");
			}
		}while(! horaValida);


		horaSaida = "";
		valorPago = 0;

		do {
			System.out.print("\nConfirma a gravação dos dados (S/N) ? ");
			confirmacao = Main.leia.next().charAt(0);
			if (confirmacao == 'S') {
				salvarVeiculo();
			}
		}while (confirmacao != 'S' && confirmacao != 'N');

	}


	/******************** MÉTODOS AUXILIARES ********************/
	
	// Método para gerar descrição(nome) da marca
	public static String gerarDescMarca (String codMarca){
		String descricao = "";
		for (byte j = 0; j < Main.descricaoMarca.length;j++){
			if (codMarca.equalsIgnoreCase(Main.codMarca[j])){
				descricao =Main.descricaoMarca[j];
			}
		}

		return descricao;
	}

	//Método de pesquisa de marca
	public static int pesquisarMarcaVeiculo (String codMarcaDigitado){
		boolean encontrou = false;
		int z;
		for (z=0; z<Main.codMarca.length; z++){
			if (codMarcaDigitado.equalsIgnoreCase(Main.codMarca[z])){
				encontrou = true;
				break;
			}
		}
		if (encontrou){
			return z;
		} else{
			return -1;
		}

	}

	// Método para deletar Registro de Veículo
	public void desativarVeiculoEstacionamento(long posicao)	{    
		// método para alterar o valor do campo ATIVO para N, tornando assim o registro excluído
		try {
			RandomAccessFile arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");

			arquivo.seek(posicao);
			arquivo.writeChar('N');   // desativar o registro antigo
			arquivo.close();
		}catch (IOException e) { 
			System.out.println("Erro na abertura do arquivo  -  programa será finalizado");
			System.exit(0);
		}

	}

	// Método para salvar Registro de Veículo
	public void salvarVeiculo() {	
		// método para incluir um novo registro no final do arquivo em disco
		try {
			RandomAccessFile arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");

			arquivo.seek(arquivo.length());  // posiciona o ponteiro no final do arquivo (EOF)
			arquivo.writeChar(ativo);
			arquivo.writeUTF(codEst);
			arquivo.writeUTF(placa);
			arquivo.writeChar(tipoOperacao);
			arquivo.writeUTF(codMarca);
			arquivo.writeUTF(modeloCor);
			arquivo.writeUTF(categoriaVeiculo);
			arquivo.writeUTF(dataOperacao);
			arquivo.writeUTF(horaEntrada);
			arquivo.writeUTF(horaSaida);
			arquivo.writeFloat(valorPago);

			arquivo.close();
			System.out.println("Dados gravados com sucesso !\n");
		}catch (IOException e) { 
			System.out.println("Erro na abertura do arquivo  -  programa será finalizado");
			System.exit(0);
		}
	}

	// Método para gerar cabeçalho de tabela de pesquisa
	public static void relatorioHeader () {
		// Estrutura do relatório de pesquisa
		System.out.println("Placa\tOP  Veículo\t     Marca        Cat.  Data  \t    Hr.Entrada   Hr.Saída    Valor Pago");
		System.out.println("-------\t--  ---------------  ---------    ----  ----------  ----------   ---------   ----------");
	}
	
	public static long pesquisaVeiculo (){
		return 00000;
	}
	
	// Método para pesquisar veículo através de seu código
	public long pesquisarVeiculoPorCodigo(String codEstPesquisa) {	
		// método para localizar um registro no arquivo em disco
		long posicaoCursorArquivo = 0;
		try { 
			RandomAccessFile arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
			while (true) {
				posicaoCursorArquivo  = arquivo.getFilePointer();	// posição do início do registro no arquivo
				ativo		 = arquivo.readChar();
				codEst       = arquivo.readUTF();
				placa        = arquivo.readUTF();
				tipoOperacao = arquivo.readChar();
				codMarca     = arquivo.readUTF();
				modeloCor    = arquivo.readUTF();
				categoriaVeiculo = arquivo.readUTF();
				dataOperacao = arquivo.readUTF();
				horaEntrada  = arquivo.readUTF();
				horaSaida    = arquivo.readUTF();
				valorPago    = arquivo.readFloat();

				if ( codEstPesquisa.equals(codEst) && ativo == 'S') {
					arquivo.close();
					return posicaoCursorArquivo;
				}
			}
		}catch (EOFException e) {
			return -1; // registro não foi encontrado
		}catch (IOException e) { 
			System.out.println("Erro na abertura do arquivo  -  programa será finalizado");
			System.exit(0);
			return -1;
		}
	}


	public static int calcularValorPago(String horaSaid){
		return 0;
	}
	

	/******************** MÉTODOS DE CONSISTÊNCIA ********************/	

	//Método consitência de hora
	public static boolean consistirHora(String hora){
		byte h;
		byte min;
		boolean valido = true;

		//Valida hora de entrada
		if(hora.charAt(2) != ':'){
			valido = false;
		}
		if(hora.charAt(0) < '0' || hora.charAt(0) > '9'){
			valido = false;
		}else{
			h = Byte.parseByte(hora.substring(0 , 2)); //pega a HORA
			min = Byte.parseByte(hora.substring(3)); //pega o MINUTO
			if(h < 0 || h > 23){
				valido = false;
			}else if(min < 0 || min > 59){
				valido = false;
			} //end if validar hora/minuto
		} //end if validação

		return valido;
	}

	//Método para consistir placa
	public static boolean consistirPlaca (String placaDigitada){
		boolean placaValida = true;
		if (placaDigitada.length() != 7){
			placaValida = false;
		} else {
			for (byte x=0; x<=6; x++){
				if (x <= 2){
					if ( placaDigitada.charAt(x) < 'A' || placaDigitada.charAt(x) > 'Z' ){
						placaValida = false;
						break;
					}
				} else{
					if  ( placaDigitada.charAt(x) < '0' || placaDigitada.charAt(x) > '9' ){
						placaValida = false;
						break;
					}
				}

			}

		}
		return placaValida;
	}

	//Método consitência de categoria
	public static String consistirCategoria (String categoriaDigitada){
		String categoria="";
		if (categoriaDigitada.equalsIgnoreCase("GI")){
			categoria = "Grande e Importado";
		} else if (categoriaDigitada.equalsIgnoreCase("PI")){
			categoria = "Pequeno e Importado";
		}else if (categoriaDigitada.equalsIgnoreCase("GN")){
			categoria = "Grande e Nacional";

		}else if (categoriaDigitada.equalsIgnoreCase("PN")){
			categoria = "Pequeno e Nacional";
		} else {
			categoria = "ERRO";
		}
		return categoria;
	}

	//Método consistência data
	public static boolean consistirData (String data){
		boolean dataOk = false;
		
		for (byte x = 0; x < data.length(); x++){
			if ((x < 2 || x < 5 || x < 9) && (data.charAt(x) >= '0' || data.charAt(x) <= '9')){
				dataOk = true;
			}else if ((x == 2 || x == 5) && data.charAt(x) == '/'){
				dataOk = true;
			}
		} 
		
		return dataOk;
	}
}
