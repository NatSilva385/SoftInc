package br.org.fatec.softinc.models;

public enum StatusOrdemServico {
    ABERTA,FINALIZADA,CANCELADA;

    public static StatusOrdemServico fromInteger(int x){
        switch (x){
            case 0:
                return ABERTA;
            case 1:
                return  FINALIZADA;
        }
        return  CANCELADA;
    }
}
