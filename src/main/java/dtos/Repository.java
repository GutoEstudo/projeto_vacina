package dtos;

import config.ConnectionFactory;

import java.sql.*;
import java.util.List;

public class Repository {
    private Connection connection;

    public Repository(Connection connection) {
        this.connection = connection;
    }

    public boolean existeLocalizacao(int codIbgeInformado) throws SQLException {

        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM localizacao WHERE cod_ibge = " + codIbgeInformado);

        return resultSet.next();

    }

    public boolean existePessoa(String cpfInformado) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM pessoa WHERE cpf = " + cpfInformado);
        return  resultSet.next();
    }

    public boolean existeVacina(int idVacina) throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM vacina WHERE id_vacina = " + idVacina);
        return  resultSet.next();
    }


    public void salvarLocalizacoes(List<Localizacao> localizacoes) throws SQLException {
        String query = "INSERT INTO localizacao (cod_ibge, nome_cidade, estado, regiao) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (Localizacao localizacao : localizacoes) {
            preparedStatement.setInt(1, localizacao.getCodIbge());
            preparedStatement.setString(2, localizacao.getNomeCidade());
            preparedStatement.setString(3, localizacao.getEstado());
            preparedStatement.setString(4, localizacao.getRegiao());
            preparedStatement.executeUpdate();
        }

    }

    public void salvarPessoas(List<Pessoa> pessoas) throws SQLException {
        String query = "INSERT INTO pessoa (cpf, nome, escolaridade, idade, localizacao) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (Pessoa pessoa :pessoas) {
            preparedStatement.setString(1, pessoa.getCpf());
            preparedStatement.setString(2,pessoa.getNome());
            preparedStatement.setString(3, pessoa.getEscolaridade());
            preparedStatement.setInt(4, pessoa.getIdade());
            preparedStatement.setInt(5, pessoa.getLocalizacao());
            preparedStatement.executeUpdate();
        }
    }

    public void salvarVacinas(List<Vacina> vacinas) throws SQLException {
        String query = "INSERT INTO vacina (id_vacina, doenca, qtd_doses, num_lote) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (Vacina vacina : vacinas) {
            preparedStatement.setInt(1, vacina.getIdVacina());
            preparedStatement.setString(2,vacina.getDoenca());
            preparedStatement.setInt(3, vacina.getQtdDoses());
            preparedStatement.setString(4, vacina.getNumLote());
            preparedStatement.executeUpdate();
        }
    }

    public void salvarRecebimentoVacina(List<Recebe> recebes) throws SQLException {
        String query = "INSERT INTO recebe (id_vacina, cpf, localizacao, nome_profissional, data) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (Recebe recebe : recebes) {
            preparedStatement.setInt(1, recebe.getVacina());
            preparedStatement.setString(2,recebe.getPessoa());
            preparedStatement.setInt(3, recebe.getLocalizacao());
            preparedStatement.setString(4, recebe.getProfissional());
            long mili = recebe.getData().getTime();
            Date dateSql = new Date(mili);
            preparedStatement.setDate(5, dateSql);
            preparedStatement.executeUpdate();
        }
    }
}
