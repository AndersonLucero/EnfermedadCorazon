package agentes;

import ambiente.Parametros;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.Utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author USRBET
 */
public class Train extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new Comportamiento());
    }

    @Override
    protected void takeDown() {
        System.out.println("");
    }

    class Comportamiento extends Behaviour {

        String rutaModelo = "src/archivos/model.train";
        String contenido = "";
        Parametros p = new Parametros(getArguments()[2].toString());
        String param = p.toString();
        int iter = Integer.parseInt(getArguments()[1].toString());
        //"-L 0.5 -M 0.1 -N 100 -V 0 -S 0 -E 20 -H 200";
        HashMap<String, Double> resultados = new HashMap<>();
        String content = "false";

        @Override
        public void action() {
//            try {
            System.out.println("Soy: " + getName());
//            Object[] a = getArguments();
//            String b = a[0].toString();
            String path = getArguments()[0].toString();

            double pctCorrect = trainSave(path, param);
            resultados.put(param, pctCorrect);

            if (pctCorrect > 85) {
                System.out.println("ENVIAR AGENTE TEST");
               doDelete();
            } else {
                iter--;
//                System.out.println("Iter:" + iter);
                if (iter != 0) {
                    //buscardor del HM
                    boolean aux = false;
                    for (Map.Entry<String, Double> entrySet : resultados.entrySet()) {
                        String key = entrySet.getKey();
                        Double value = entrySet.getValue();
//                        System.out.println("K: " + key + " V: " + value);
                        if (pctCorrect >= value) {
                            aux = true;
                        } else {
                            aux = false;
                        }
                    }
                    if (aux && (!Boolean.parseBoolean(content))) {
                        System.out.println(param);
                        param = reajustarNeuronas(p).toString();
                    } else {
                        param = reajustarMomentum(p, content).toString();
                    }
                    p = new Parametros(param);

//                    doDelete();
                } else {
                    double pctaux = 0;
                    String key = "";
                    for (Map.Entry<String, Double> entrySet : resultados.entrySet()) {

                        Double value = entrySet.getValue();
                        if (value > pctaux) {
                            pctaux = value;
                            key = entrySet.getKey();
                        }
                    }
                    System.out.println("Mayor: " + pctaux);
                    //send msj
                    sendMsj(pctaux);
                    ACLMessage acl = blockingReceive();
                    p = new Parametros(key);
                    content = acl.getContent();
                    iter = Integer.parseInt(getArguments()[1].toString());
                    resultados.clear();

                    //asiganar valor mas alto
                    //enviar al agente
                }

            }
            //"-L 0.5 -M 0.1 -N 100 -V 0 -S 0 -E 20 -H 200"
            //comunicacion
//                AID aid = new AID();//identificador
//                aid.setLocalName("");
//                
//                ACLMessage acl = new ACLMessage(ACLMessage.REQUEST);
//                acl.addReceiver(aid);
//                acl.setSender(getAID());
//                //acl.setContent(contenido);
//                acl.setContentObject(new Ambiente(30,80,3000,1.5,500));
//                acl.setConversationId("1-2");
//                acl.setLanguage("henry");
//                send(acl);
//                //doDelete();//matar al agente
//                ACLMessage acl2 = blockingReceive();
//                System.out.println(acl2);
//                if (acl2.getContent().equals("Nada, porque wey")) {
//                    contenido = "no, por nada carnal";
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(Agente1.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }

        public void sendMsj(double pct) {
            AID aid = new AID();//identificador
            aid.setLocalName("Test");
            ACLMessage acl = new ACLMessage(ACLMessage.REQUEST);
            acl.addReceiver(aid);
            acl.setSender(getAID());
            acl.setContent(pct + " " + rutaModelo);
            acl.setConversationId("1-2");
            acl.setLanguage("ann");
            send(acl);
        }

        public Parametros reajustarNeuronas(Parametros p) {
            p.setH(((int) (p.getH() * 1.02)));
            return p;
        }

        public Parametros reajustarMomentum(Parametros p, String content) {
            if ((!Boolean.parseBoolean(content))) {
                p.setM(p.getM() * 1.02);
                p.setL(p.getL() * 1.05);
            } else {
                p.setM(p.getM() * 1.1);
                p.setL(p.getL() * 0.90);
            }

            return p;
        }

        public double trainSave(String path, String params) {
            //lectura del archivo
            double pctCorrect = 0;
            try {
                FileReader fileReader = new FileReader(path);

                //crear las instancias
                Instances instances = new Instances(fileReader);
                //definir en la instancias donde esta la etiqueta
                instances.setClassIndex(instances.numAttributes() - 1);

                //construir el modelo
                //MLP
                MultilayerPerceptron net = new MultilayerPerceptron();
                net.setOptions(Utils.splitOptions(params));
                net.buildClassifier(instances);

                //guardarArchivo .model
                Debug.saveToFile(rutaModelo, net);

                Evaluation ev = new Evaluation(instances);
                ev.evaluateModel(net, instances);

//                System.out.println(ev.toMatrixString("Matriz de Conf."));
//                System.out.println(ev.toSummaryString());
                pctCorrect = ev.pctCorrect();

            } catch (Exception ex) {
                Logger.getLogger(Train.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pctCorrect;
        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
