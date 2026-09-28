package org.teiacoltec.poo.atv10;

import java.util.HashMap;
import java.util.Map;

public class ColecaoClientes {
    
    private static final Map<Integer, Usuario> ClientesHashMap = new HashMap<>();
    
    public static void AdicionarCliente(Usuario cliente) {
        ClientesHashMap.put(cliente.getID(), cliente);
    }

    public static void RemoverCliente(int id) {
        ClientesHashMap.remove(id);
    }

    public static Usuario BuscarCliente(int id) {
        return ClientesHashMap.get(id);
    }
    
    public static Map<Integer, Usuario> ObtemMapaClientes() {
        return ClientesHashMap;
    }
    
    public static void Limpar() {
        ClientesHashMap.clear();
    }
}