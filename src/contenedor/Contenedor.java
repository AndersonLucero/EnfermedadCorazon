/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contenedor;

import java.util.logging.Level;
import java.util.logging.Logger;
import agentes.*;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USRBET
 */
public class Contenedor {

    AgentContainer agentContainer;
    AgentController agentController;

    public void iniciarContenedor() {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        runtime.setCloseVM(true);
        //
        Profile profile = new ProfileImpl(null, 1099, null);
        agentContainer = runtime.createMainContainer(profile);
        iniciarAgentes();
    }

    private void iniciarAgentes() {
        try {
            Object[] train = {"src/archivos/hearttrain.arff", 10, "-L 0.5 -M 0.1 -N 100 -V 0 -S 0 -E 20 -H 200"};
            Object[] test = {"src/archivos/hearttest.arff"};
            agentContainer.createNewAgent("Train", Train.class.getName(), train).start();
            agentContainer.createNewAgent("Test", Test.class.getName(), test).start();
            //agentContainer.createNewAgent("Luchito2", Agente2.class.getName(), null).start();
            //agentContainer.createNewAgent("Luchito", Agente1.class.getName(), null).start();
        } catch (StaleProxyException ex) {
            Logger.getLogger(Contenedor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ControllerException ex) {
            Logger.getLogger(Contenedor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
