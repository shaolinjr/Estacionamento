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
	boolean placaValida; //VariÃ¡vel nova
	int marcaValida; //VariÃ¡vel nova
	char confirmacao; //VariÃ¡vel nova
	String codEstChave; //Variavel nova
	static long posicaoRegistro = 0; //Variavel nova


	static RandomAccessFile arquivo;

	/******************** MÃ‰TODOS PRINCIPAIS ********************/


	public void saidaVeiculo() { //AlteraÃ§Ã£o
		
		do {
    		System.out.println(" ***************  SAÃ�DA DE VEÃ�CULOS  ***************** ");
    		do {
    			//Main.leia.nextLine();
    			System.out.print("Digite o cÃ³digo do veÃŒculo que deseja dar saÃ­da ( FIM para encerrar ): ");
    			codEstChave = Main.leia.next();
    			if (codEstChave.equals("FIM")) {
    				break;
    			}
    			posicaoRegistro = pesquisarVeiculoPorCodigo(codEstChave);
//    			Main.leia.nextLine();
   				if (posicaoRegistro == -1) {
   					System.out.println("VeÃŒculo nÃ£o cadastrado no arquivo, digite outro cÃ³digo\n");
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
    		
    		System.out.print("Digite a data de operaÃ§Ã£o......................:");
    		dataOperacao = Main.leia.next();

    		boolean horaValida;
    		do{
    			System.out.print("Digite a hora de saÃ­da.......................:");
    			horaSaida = Main.leia.next();
    			horaValida = consistirHora(horaSaida);
    			if (! horaValida){
    				System.out.println("Hora invÃ¡lida. ");
    			}
    		}while(! horaValida);
    		
    		valorPago = calcularValorPago(horaEntrada, horaSaida, categoriaVeiculo);
    		System.out.print("Valor a pagar: " + valorPago);
    		do {
    			System.out.print("\nConfirma a saÃ­da e pagamento do veÃ­culo (S/N) ? ");
    			confirmacao = Main.leia.next().charAt(0);
    			if (confirmacao == 'S') {
    				desativarVeiculoEstacionamento(posicaoRegistro);
    				tipoOperacao = 'S';
    				salvarVeiculo();
    			}
    		}while (confirmacao != 'S' && confirmacao != 'N');
    		
		}while(codEstChave.equals("FIM"));
	}


	public void excluir(){
   	 
    	do {
    		System.out.println("\n ***************  EXCLUSÃƒO DE VEÃ�CULOS  ***************** ");
    		do {
    			//Main.leia.nextLine();
    			System.out.print("Digite o cÃ³digo do veÃŒculo que deseja excluir ( FIM para encerrar ): ");
    			codEstChave = Main.leia.next();
    			if (codEstChave.equals("FIM")) {
    				break;
    			}
    			posicaoRegistro = pesquisarVeiculoPorCodigo(codEstChave);
//    			Main.leia.nextLine();
   				if (posicaoRegistro == -1) {
   					System.out.println("VeÃŒculo nÃ£o cadastrado no arquivo, digite outro cÃ³digo\n");
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
	    		System.out.print("\nConfirma a exclusÃ£o deste veÃ­culo (S/N) ? ");
	    		confirmacao = Main.leia.next().charAt(0);
	    		if (confirmacao == 'S') {
	    			desativarVeiculoEstacionamento(posicaoRegistro);
	    			System.out.println("VeÃ­culo excluÃ­do.\n");
	    		 }
	    	}while (confirmacao != 'S' && confirmacao != 'N');

    	}while ( ! codEstChave.equals("FIM"));
	}

	public void relatorioFaturamento (){
		byte opcao;
		String placaDigitada;
		String dataDigitada;
		boolean encontrouRegistros = false;
		do {
			System.out.println("\n****************** RELATÓRIO DE VEÍCULOS ****************** ");
			System.out.println("[0] Sair");
			System.out.println("[1] Relatório por veículo");
			System.out.println("[2] Relatório por data");
			System.out.println("[3] Relatório geral");
			
			System.out.print("Digite a opção desejada: ");
			
			opcao = Main.leia.nextByte();
			if (opcao == 0){
				System.out.println("Saindo...");
				break;
			}
			else if(opcao < 0 || opcao > 3){
				System.out.println("Opção inválida! Digite novamente.");
			}
			
		}while (opcao < 0 || opcao > 3);
		
		switch (opcao){
			case 1: 
				do {
					System.out.print("Digite a placa: ");
					placaDigitada = Main.leia.next();
					
					if (!consistirPlaca(placa)){
						System.out.println("Placa inválida! Digite novamente.");
					}
				}while(!consistirPlaca(placa));
				
				try {
					arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
					
					while (true) {
						posicaoRegistro  = arquivo.getFilePointer();	// posiÃ§Ã£o do inÃ­cio do registro no arquivo
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
						
						if (ativo == 'S' && tipoOperacao == 'S' && placaDigitada.equals(placa)){
							relatorioHeader();
							System.out.println(placa+" " + tipoOperacao +"  " +" "+ 
									modeloCor +"\t     "+ gerarDescMarca(codMarca) +"\t  " +categoriaVeiculo +"\t"+ 
									dataOperacao +"     "+ horaEntrada +"     "+ horaSaida +"\t\t"+ valorPago);
							
						}
					}
					
				}catch (EOFException e){
					System.out.println("Placa não encontrada nos registros.");
				}catch (IOException e){
					System.out.println("Erro ao tentar abrir o programa. Saindo...");
					System.exit(0);
				}
				break;
			case 2:
			
				System.out.print("Digite a data de operação: ");
				dataDigitada = Main.leia.next();
	
				try {
					arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
					
					while (true) {
						posicaoRegistro  = arquivo.getFilePointer();	// posiÃ§Ã£o do inÃ­cio do registro no arquivo
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
						
						if (ativo == 'S' && tipoOperacao == 'S' && dataDigitada.equals(dataOperacao)){
							relatorioHeader();
							System.out.println(placa+" " + tipoOperacao +"  " +" "+ 
									modeloCor +"\t     "+ gerarDescMarca(codMarca) +"\t  " +categoriaVeiculo +"\t"+ 
									dataOperacao +"     "+ horaEntrada +"     "+ horaSaida +"\t\t"+ valorPago);
							break;
						}
					}
					
				}catch (EOFException e){
					System.out.println("Placa não encontrada nos registros.");
				}catch (IOException e){
					System.out.println("Erro ao tentar abrir o programa. Saindo...");
					System.exit(0);
				}
				break;
			case 3:
				relatorioHeader();
				try {
					arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
					
					while (true) {
						posicaoRegistro  = arquivo.getFilePointer();	// posiÃ§Ã£o do inÃ­cio do registro no arquivo
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
						
						if (ativo == 'S' && tipoOperacao == 'S'){
							encontrouRegistros = true;
							
							System.out.println(placa+" " + tipoOperacao +"  " +" "+ 
									modeloCor +"\t     "+ gerarDescMarca(codMarca) +"\t  " +categoriaVeiculo +"\t"+ 
									dataOperacao +"     "+ horaEntrada +"     "+ horaSaida +"\t\t"+ valorPago);
							
						}
					}
					
				}catch (EOFException e){
					if (!encontrouRegistros){
						System.out.println("Registros não encontrados.");
					}
				}catch (IOException e){
					System.out.println("Erro ao tentar abrir o programa. Saindo...");
					System.exit(0);
				}
				break;
		}
	}

	// Metodo de consulta de veÃ­culos
	public static void consultar (){
		
		byte escolha = -1;
		boolean encontrouAlgumRegistro = false;
		
		System.out.println("****************** CONSULTA  DE VEÃ�CULOS ******************");

		do {
			System.out.println("Escolha a opÃ§Ã£o que deseja realizar, digite 0 para sair.");
			System.out.println("\n[ 1 ] Exibir todos os registros");
			System.out.println("[ 2 ] Exibir somente os veÃ­culos que ainda nÃ£o saÃ­ram do estacionamento");
			System.out.println("[ 3 ] Exibir somente os veÃ­culos cadastrados em uma data especÃ­fica");
			System.out.print("\nO que deseja fazer?: ");
			escolha = Main.leia.nextByte();

			if (escolha < 0){
				System.out.println("OpÃ§Ã£o InvÃ¡lida. Digite novamente.");
			}else if (escolha == 0){
				System.out.println("AtÃ© mais...");
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
						System.out.println("NÃ£o foi encontrado nenhum registro.");
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
						System.out.println("NÃ£o foi encontrado nenhum registro.");
					}
				}catch (IOException e){
					System.out.println("Ocorreu um erro ao tentar abrir o arquivo. Finalizando o programa.");
					System.exit(0);
				}
				break;
			case 3:
	
				String data = "";
	
	
				System.out.print("Digite a data para gerar relatÃ³rio: ");
				data = Main.leia.next();
				if (!consistirData(data)){
					System.out.println("Data invÃ¡lida! Formato: dd/mm/yyy");
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
					System.out.println("NÃ£o foi encontrado nenhum registro.");
				}
			}catch (IOException e){
				System.out.println("Ocorreu um erro ao tentar abrir o arquivo. Finalizando o programa.");
				System.exit(0);
			}
			break;
		}



	}

	

	public void entradaVeiculo() { //InclusÃ£o
		//gerar cÃ³digo sequencial
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
			System.out.println("Erro na abertura do arquivo  -  programa serÃ¡ finalizado");
			System.exit(0);
		}
		System.out.println("CÃ³digo de Estacionamento: " + codEst);
		ativo = 'S';

		do {
			System.out.print("Digite a placa do carro.............: ");
			placa = Main.leia.next();
			placaValida = consistirPlaca(placa);
			if (! placaValida){
				System.out.println("A placa digitada Ã© invÃ¡lida.");
			}
		} while(! placaValida);

		tipoOperacao = 'E';
		System.out.println("Tipo de operaÃ§Ã£o...................: E - Entrada");

		System.out.println("Marcas: BM, VW, FO,MB,CV, FI, AU, TO, HO, HY");
		do {
			System.out.print("Digite o cÃ³digo da marca..............:");
			codMarca = Main.leia.next();
			marcaValida = pesquisarMarcaVeiculo(codMarca);
			if (marcaValida < 0){
				System.out.println("Marca invÃ¡lida. ");
			}
		} while(marcaValida < 0);

		Main.leia.nextLine();

		do {
			System.out.print("Digite o modelo/cor......................:");
			modeloCor = Main.leia.nextLine();
			if(modeloCor.length()<10){
				System.out.println("O campo deverÃ¡ ter no mÃ­nimo 10 caracteres.");
			}
		}while(modeloCor.length()<10);

		do {
			System.out.println("Categorias: "
					+ "\nGI â€“ Grande e Importado"
					+ "\nPI â€“ Pequeno e Importado"
					+ "\nGN â€“ Grande e Nacional"
					+ "\nPN â€“ Pequeno e Nacional");
			System.out.print("Digite a categoria.........................:");
			categoriaVeiculo = Main.leia.nextLine();
			System.out.println(consistirCategoria(categoriaVeiculo));
			if (consistirCategoria(categoriaVeiculo).equalsIgnoreCase("ERRO")){
				System.out.println("Categoria invÃ¡lida.");
			}
		} while (consistirCategoria(categoriaVeiculo).equalsIgnoreCase("ERRO"));

		System.out.print("Digite a data de operaÃ§Ã£o......................:");
		dataOperacao = Main.leia.next();

		boolean horaValida;
		do{
			System.out.print("Digite a hora de entrada.......................:");
			horaEntrada = Main.leia.next();
			horaValida = consistirHora(horaEntrada);
			
			if (! horaValida){
				System.out.println("Hora invÃ¡lida. ");
			}
		}while(! horaValida);


		horaSaida = "";
		valorPago = 0;

		do {
			System.out.print("\nConfirma a gravaÃ§Ã£o dos dados (S/N) ? ");
			confirmacao = Main.leia.next().charAt(0);
			if (confirmacao == 'S') {
				salvarVeiculo();
			}
		}while (confirmacao != 'S' && confirmacao != 'N');

	}


	/******************** MÃ‰TODOS AUXILIARES ********************/
	
	// MÃ©todo para gerar descriÃ§Ã£o(nome) da marca
	public static String gerarDescMarca (String codMarca){
		String descricao = "";
		for (byte j = 0; j < Main.descricaoMarca.length;j++){
			if (codMarca.equalsIgnoreCase(Main.codMarca[j])){
				descricao =Main.descricaoMarca[j];
			}
		}

		return descricao;
	}

	//MÃ©todo de pesquisa de marca
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

	// MÃ©todo para deletar Registro de VeÃ­culo
	public void desativarVeiculoEstacionamento(long posicao)	{    
		// mÃ©todo para alterar o valor do campo ATIVO para N, tornando assim o registro excluÃ­do
		try {
			RandomAccessFile arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");

			arquivo.seek(posicao);
			arquivo.writeChar('N');   // desativar o registro antigo
			arquivo.close();
		}catch (IOException e) { 
			System.out.println("Erro na abertura do arquivo  -  programa serÃ¡ finalizado");
			System.exit(0);
		}

	}

	// MÃ©todo para salvar Registro de VeÃ­culo
	public void salvarVeiculo() {	
		// mÃ©todo para incluir um novo registro no final do arquivo em disco
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
			System.out.println("Erro na abertura do arquivo  -  programa serÃ¡ finalizado");
			System.exit(0);
		}
	}

	// MÃ©todo para gerar cabeÃ§alho de tabela de pesquisa
	public static void relatorioHeader () {
		// Estrutura do relatÃ³rio de pesquisa
		System.out.println("Placa\tOP  VeÃ­culo\t     Marca        Cat.  Data  \t    Hr.Entrada   Hr.SaÃ­da    Valor Pago");
		System.out.println("-------\t--  ---------------  ---------    ----  ----------  ----------   ---------   ----------");
	}
	
	// Que metodo Ã© esse?!
//	public static long pesquisaVeiculo (){
//		return 00000;
//	}
	
	// MÃ©todo para pesquisar veÃ­culo atravÃ©s de seu cÃ³digo
	public long pesquisarVeiculoPorCodigo(String codEstPesquisa) {	
		// mÃ©todo para localizar um registro no arquivo em disco
		
		try { 
			RandomAccessFile arquivo = new RandomAccessFile("ESTACIONAMENTO.DAT", "rw");
			while (true) {
				posicaoRegistro  = arquivo.getFilePointer();	// posiÃ§Ã£o do inÃ­cio do registro no arquivo
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

				if ( codEstPesquisa.equalsIgnoreCase(codEst) && ativo == 'S') {
					arquivo.close();
					return posicaoRegistro;
				}
			}
		}catch (EOFException e) {
			return -1; // registro nÃ£o foi encontrado
		}catch (IOException e) { 
			System.out.println("Erro na abertura do arquivo  -  programa serÃ¡ finalizado");
			System.exit(0);
			return -1;
		}
	}

	
	public static float calcularValorPago(String horaEntra,String horaSai,String catVeiculo){
		
		float tempoPermanencia;
		float valorAPagar = 0;
		byte HoraSaida,HoraEntrada;
		byte MinSaida,MinEntrada;
		String HS,HE;//HS-hora saida  HE-hora entrada
		String MS,ME;//MS-minuto saida  ME-minuto entrada
		
		HE = horaEntra.substring(0,2);
		ME = horaEntra.substring(3,4);
		
		HS = horaSai.substring(0,2);
		MS = horaSai.substring(3,4);
		
		HoraSaida = Byte.parseByte(HS);
		HoraEntrada = Byte.parseByte(HE);
		MinSaida = Byte.parseByte(MS);
		MinEntrada = Byte.parseByte(ME);
		
		//variavel criada para facilitar os calculos
		tempoPermanencia = (HoraSaida - HoraEntrada+(MinSaida - MinEntrada)/60);
		
		//calculos matematicos
		if(HoraEntrada <= 18){
			if(catVeiculo.equals("PI")){
				valorAPagar = (float) (tempoPermanencia * 8.20);
			}else if(catVeiculo.equals("GI")){
				valorAPagar = (float) (tempoPermanencia * 10.00);
			}else if(catVeiculo.equals("PN")){
				valorAPagar = (float) (tempoPermanencia * 7.00);
			}else{
				valorAPagar = (float) (tempoPermanencia * 9.00);
			}
		}
		
		if(HoraEntrada > 18){
			if(catVeiculo.equals("PI")){
				valorAPagar = (float) (tempoPermanencia * 6.50);
			}else if(catVeiculo.equals("GI")){
				valorAPagar = (float) (tempoPermanencia * 8.00);
			}else if(catVeiculo.equals("PN")){
				valorAPagar = (float) (tempoPermanencia * 6.00);
			}else{
				valorAPagar = (float) (tempoPermanencia * 7.50);
			}
		}
		
		
			
		return valorAPagar;
	}
	

	/******************** MÃ‰TODOS DE CONSISTÃŠNCIA ********************/	

	//MÃ©todo consitÃªncia de hora
	public static boolean consistirHora(String hora){
		byte h;
		byte min;
		boolean valido = true;

		//Valida hora de entrada
		if(hora.charAt(2) != ':'){
			valido = false;
		}
		if(hora.charAt(0) < '0' || hora.charAt(0) > '2'){
			valido = false;
		}else if(hora.charAt(1) < '0' || hora.charAt(1) > '9'){
			valido = false;
		}else{
			h = Byte.parseByte(hora.substring(0 , 2)); //pega a HORA
			min = Byte.parseByte(hora.substring(3)); //pega o MINUTO
			if(h < 0 || h > 23){
				valido = false;
			}else if(min < 0 || min > 59){
				valido = false;
			} //end if validar hora/minuto
		} //end if validaÃ§Ã£o

		return valido;
	}

	//MÃ©todo para consistir placa
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

	//MÃ©todo consitÃªncia de categoria
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

	//MÃ©todo consistÃªncia data
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
