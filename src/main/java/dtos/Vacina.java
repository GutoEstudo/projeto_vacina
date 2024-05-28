package dtos;

public class Vacina {
    private int idVacina;
    private String doenca;
    private int qtdDoses;
    private String numLote;

    public int getIdVacina() {
        return idVacina;
    }

    public void setIdVacina(int idVacina) {
        this.idVacina = idVacina;
    }

    public String getDoenca() {
        return doenca;
    }

    public void setDoenca(String doenca) {
        this.doenca = doenca;
    }

    public int getQtdDoses() {
        return qtdDoses;
    }

    public void setQtdDoses(int qtdDoses) {
        this.qtdDoses = qtdDoses;
    }

    public String getNumLote() {
        return numLote;
    }

    public void setNumLote(String numLote) {
        this.numLote = numLote;
    }

}
