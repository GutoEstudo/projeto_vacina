package org.example;

import com.mysql.cj.util.EscapeTokenizer;
import config.ConnectionFactory;
import dtos.*;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Scanner lerComEspacos = new Scanner(System.in).useDelimiter("\n");
    private static List<Vacina> listaVacinas = new ArrayList<>();
    private static List<Pessoa> listaPessoas = new ArrayList<>();
    private static List<Localizacao> listaLocalizacoes = new ArrayList<>();
    private static List<Recebe> listaRecebimentoVacina = new ArrayList<>();
    private static Repository repository;
    private static Connection connection;
    public static void main(String[] args) throws SQLException, ParseException {

        if (connection == null || connection.isClosed()) {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connection = connectionFactory.createConnection();
        }
        repository = new Repository(connection);

        while (true) {

            System.out.println("Bem Vindo a aplicação para registros de vacinas!");
            System.out.println("O que deseja fazer?");
            System.out.println("1. Registrar vacina");
            System.out.println("2. Registrar pessoa");
            System.out.println("3. Registrar local");
            System.out.println("4. Registrar recebimento de vacina");
            System.out.println("5. Mostrar informações registradas");
            System.out.println("6. Salvar as informacoes registradas no banco");

            int opcaoEscolhida;

            if (scanner.hasNextInt()) {
                opcaoEscolhida = scanner.nextInt();
            } else {
                opcaoEscolhida = 99;
                scanner.nextInt();
            }

            switch (opcaoEscolhida) {
                case 1:
                    registraVacina();
                    continue;
                case 2:
                    registrarPessoa();
                    continue;
                case 3:
                    registrarLocalizacao();
                    continue;
                case 4:
                    salvarRecebimentoDeVacina();
                    continue;
                case 5:
                    mostrarInfos();
                    continue;
                case 6:
                    salvarInfosNoBanco();
                    continue;
                default:
                    System.out.println("Nnehuma das opcoes selecionadas. Tente novamente!");
            }
        }
    }

    private static void registraVacina() {
        Vacina vacina = new Vacina();

        boolean numLoteInvalido = true;
        while (numLoteInvalido) {
            System.out.println("Informe o numero do lote da vacina:");
            String numLoteInformado = lerComEspacos.next();
            if (!numLoteInformado.isEmpty() && numLoteInformado.length() <= 11) {
                vacina.setNumLote(numLoteInformado);
                numLoteInvalido = false;
            } else {
                System.out.println("O numero do lote informado nao e valido. Tente novamente");
            }
        }

        boolean doencaInvalida = true;
        String doencaInformada;
        while (doencaInvalida) {
            System.out.println("Digite o nome da doença que a vacina ajuda a curar:");
            doencaInformada = lerComEspacos.next();

            if (doencaInformada.length() <= 45) {
                vacina.setDoenca(doencaInformada);
                doencaInvalida = false;
            } else {
                System.out.println("O nome da doenca precisa ter menos de 45 caracteres");
            }

        }

        int qtdDosesInformada;
        boolean qtdDosesInvalido = true;
        while (qtdDosesInvalido) {
            System.out.println("Informe a quantidade de doses necessario para vacina:");

            if (!scanner.hasNextInt()) {
               scanner.nextInt();
               continue;
            }
            qtdDosesInformada = scanner.nextInt();
            vacina.setQtdDoses(qtdDosesInformada);
            qtdDosesInvalido = false;

        }

        listaVacinas.add(vacina);

    }

    private static void registrarLocalizacao() throws SQLException {

        Localizacao localizacao = new Localizacao();

        boolean codigoIbgeInvalido = true;
        while (codigoIbgeInvalido) {
            System.out.println("Informe o código IBGE da cidade:");
            if (!scanner.hasNextInt()) {
                System.out.println(" O codigo IBGE informado nao e valido. Tente novamente!");
                scanner.nextInt();
                continue;
            }
            int codIbgeInformado = scanner.nextInt();
            if (repository.existeLocalizacao(codIbgeInformado)) {
                System.out.println("O codigo IBGE inforado ja existe no banco de dados");
                continue;
            } else if (existeLocalizacaoNaLista(codIbgeInformado)) {
                System.out.println("O codigo IBGE informado ja esta registrado e pronto para ser salvo no banco de dados");
                continue;
            }
            localizacao.setCodIbge(codIbgeInformado);
            codigoIbgeInvalido = false;
        }


        boolean nomeCidadeInvalido = true;
        while (nomeCidadeInvalido) {
            System.out.println("Informe o nome da cidade:");
            String nomeCidadeInformado = lerComEspacos.next();

            if (nomeCidadeInformado.isEmpty() || nomeCidadeInformado.length() > 45) {
                System.out.println("O nome da cidade informado não é válido. Tente novamente!");
                continue;
            }

            localizacao.setNomeCidade(nomeCidadeInformado);
            nomeCidadeInvalido = false;
        }


        boolean estadoInvalido = true;
        while (estadoInvalido) {
            System.out.println("Informe o nome do estado:");
            String estadoInformado = lerComEspacos.next();

            if (estadoInformado.isEmpty() || estadoInformado.length() > 45) {
                System.out.println("O nome do estado informado não é válido. Tente novamente!");
                continue;
            }

            localizacao.setEstado(estadoInformado);
            estadoInvalido = false;
        }


        boolean regiaoInvalida = true;
        while (regiaoInvalida) {
            System.out.println("Informe o nome da regiao:");
            String regiaoInformada = lerComEspacos.next();

            if (regiaoInformada.isEmpty() || regiaoInformada.length() > 45) {
                System.out.println("O nome da regiao informada não é válida. Tente novamente!");
                continue;
            }
            localizacao.setRegiao(regiaoInformada);
            regiaoInvalida = false;
        }

        listaLocalizacoes.add(localizacao);
    }

    private static void registrarPessoa() throws SQLException {

        Pessoa pessoa = new Pessoa();

        boolean cpfInvalido = true;
        while (cpfInvalido) {
            System.out.println("Informe o CPF da pessoa:");
            String cpfInformado = lerComEspacos.next();
            if (cpfInformado.length() != 11) {
                System.out.println("O CPF informado nao e valido. Tente novamente!");
                continue;
            } else if (repository.existePessoa(cpfInformado)) {
                System.out.println("O CPF informado ja existe no banco de dados");
                continue;
            } else if (existePessoaNaLista(cpfInformado)) {
                System.out.println("O CPF informado já está registrdo e ja esta pronto para ser salvo no bacno de dados!");
                return;
            }
            pessoa.setCpf(cpfInformado);
            cpfInvalido = false;
        }

        boolean nomeInvalido = true;
        while (nomeInvalido) {
            System.out.println("Informe o nome da pessoa:");
            String nomeInformado = lerComEspacos.next();

            if (nomeInformado.isEmpty() || nomeInformado.length() > 45) {
                System.out.println("O nome informado nao e valido. Tente novamente!");
                continue;
            }
            pessoa.setNome(nomeInformado);
            nomeInvalido = false;
        }


        boolean escolaridadeInvalido = true;
        while (escolaridadeInvalido) {
            System.out.println("Informe a escolaridade da pessoa:");
            String escolaridadeInformada = lerComEspacos.next();

            if (escolaridadeInformada.isEmpty() || escolaridadeInformada.length() > 45) {
                System.out.println(" A escolaridade informada nao e valida. Tente novamente!");
                continue;
            }
            pessoa.setEscolaridade(escolaridadeInformada);
            escolaridadeInvalido = false;
        }

        boolean idadeInvalido = true;
        while (idadeInvalido) {
            System.out.println("Informe a idade da pessoa:");
            if (!scanner.hasNextInt()) {
                System.out.println(" A idade informada nao e valida. Tente novamente!");
                scanner.next();
                continue;
            }
            int idadeInformada = scanner.nextInt();
            pessoa.setIdade(idadeInformada);
            idadeInvalido = false;
        }

        boolean localizacaoInvalida = true;
        while (localizacaoInvalida) {
            System.out.println("Informe o codigo IBGE da localizacao da pessoa.");
            if (!scanner.hasNextInt()) {
                System.out.println(" A localizacao informada nao e valida. Tente novamente!");
                scanner.next();
                continue;
            }

            int localizacaoInformada = scanner.nextInt();

            if (localizacaoInformada < 0) {
                return;
            } else if (!repository.existeLocalizacao(localizacaoInformada)) {
                System.out.println(" A localizacao informada nao esta registrada no banco. Tente novamente ou digite -1 para" +
                        "cancelar operacao e voltar quando tiver registrado a localizacao!");
                continue;

            }

            pessoa.setLocalizacao(localizacaoInformada);
            localizacaoInvalida = false;
        }

        listaPessoas.add(pessoa);
    }

    private static void salvarRecebimentoDeVacina() throws SQLException, ParseException {
        Recebe recebe = new Recebe();

        System.out.println("Informe o id da vacina:");
        if (!scanner.hasNextInt()) {
            scanner.nextInt();
            return;
        }

        int idVacinaInformado = scanner.nextInt();
        if(repository.existeVacina(idVacinaInformado)) {
            recebe.setVacina(idVacinaInformado);
        } else {
            System.out.println("A vacina nao existe no banco de dados. Por favor, tente novamente ou" +
                    " salve no banco e depois volte aqui.");
            return;
        }

        System.out.println("Informe o cpf da pessoa vacinada:");
        String cpfInformado = lerComEspacos.next();
        if(repository.existePessoa(cpfInformado)) {
            recebe.setPessoa(cpfInformado);
        } else {
            System.out.println("A pessoa nao existe no banco de dados. Por favor, tente novamente ou" +
                    " salve no banco e depois volte aqui.");
            return;
        }

        System.out.println("Informe o codigo ibge da localizacao em que a pessoa foi vacinada:");
        if (!scanner.hasNextInt()) {
            scanner.nextInt();
            return;
        }

        int codIbgeInformado = scanner.nextInt();
        if (repository.existeLocalizacao(codIbgeInformado)) {
            recebe.setLocalizacao(codIbgeInformado);
        } else {
            System.out.println("A localizacao nao existe no banco de dados. Por favor, tente novamente ou" +
                    " salve no banco e depois volte aqui.");
            return;
        }

        boolean profissionalInvalido = true;
        while (profissionalInvalido) {
            System.out.println("Informe o nome do profissional:");
            String profissionalInformado = lerComEspacos.next();

            if (profissionalInformado.isEmpty() ||profissionalInformado.length() > 45) {
                System.out.println(" A escolaridade informada nao e valida. Tente novamente!");
                continue;
            }
            recebe.setProfissional(profissionalInformado);
            profissionalInvalido = false;
        }

        boolean dataInvalida = true;
        while (dataInvalida) {
            System.out.println("Informe a data da vacinacao no formato: dd/MM/aa");
            String dataInformada = lerComEspacos.next();
            if (!dataInformada.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$")) {
                System.out.println("A data nao foi informada no padrao esperado (dd/mm/aaaa). Tente novamente!");
                continue;
            }
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("dd/mm/yyyy");
            recebe.setData(sdf.parse(dataInformada));
            dataInvalida = false;
        }
        listaRecebimentoVacina.add(recebe);
    }

    private static boolean existePessoaNaLista(String cpfInformado) {
        for (Pessoa pessoa : listaPessoas) {
            if (pessoa.getCpf().equals(cpfInformado)) {
                return true;
            }
        }
        return false;
    }

    private static boolean existeLocalizacaoNaLista(int codIbgeInformado) {
        for (Localizacao localizacao : listaLocalizacoes) {
            if (localizacao.getCodIbge() == codIbgeInformado) {
                return true;
            }
        }
        return false;
    }

    private static void salvarInfosNoBanco() throws SQLException {
        if (!listaLocalizacoes.isEmpty()) {
            repository.salvarLocalizacoes(listaLocalizacoes);
            listaLocalizacoes.clear();
        }

        if(!listaPessoas.isEmpty()) {
            repository.salvarPessoas(listaPessoas);
            listaPessoas.clear();
        }

        if(!listaVacinas.isEmpty()) {
            repository.salvarVacinas(listaVacinas);
            listaVacinas.clear();
        }

        if(!listaRecebimentoVacina.isEmpty()) {
            repository.salvarRecebimentoVacina(listaRecebimentoVacina);
            listaRecebimentoVacina.clear();
        }
    }

    private static void mostrarInfos() {
        if (!listaPessoas.isEmpty()) {
            System.out.println("----------------------------------------------");
            System.out.println("Pessoas prontas para serem salvas no banco de dados:");
            for (Pessoa pessoa : listaPessoas) {
                System.out.println("CPF:" + pessoa.getCpf());
            }
        }

        if(!listaLocalizacoes.isEmpty()) {
            System.out.println("----------------------------------------------");
            System.out.println("Localizacoes prontas para serem salvas no banco de dados:");
            for (Localizacao localizacao : listaLocalizacoes) {
                System.out.println("Codigo IBGE:" + localizacao.getCodIbge());
            }
        }

        if(!listaVacinas.isEmpty()) {
            System.out.println("----------------------------------------------");
            System.out.println("Vacinas prontas para serem salvas no banco de dados:");
            for (Vacina vacina : listaVacinas) {
                System.out.println("Lote da vacina:" + vacina.getNumLote());
            }
        }
    }

}