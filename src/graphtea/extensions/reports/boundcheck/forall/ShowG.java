package graphtea.extensions.reports.boundcheck.forall;

import graphtea.graph.graph.Edge;
import graphtea.graph.graph.GraphModel;
import graphtea.graph.graph.Vertex;
import graphtea.plugins.graphgenerator.core.PositionGenerators;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by rostam on 30.09.15.
 */
public class ShowG {
    public static ProcessBuilder getShowGProcess(String file) {
        String cur = null;
        try {
            cur = new java.io.File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProcessBuilder process;

        if(System.getProperty("os.name").contains("Win")) {
            cur =cur + "\\graphs\\";
            process = new ProcessBuilder(cur + "showg_win32.exe", cur + file+".g6");
        } else {
            cur =cur + "/graphs/";
            process = new ProcessBuilder(cur + "showg.out", cur + file+".g6");
        }
        return process;
    }

    public static BufferedReader showG(String file) {
        try {
            ProcessBuilder process = getShowGProcess(file);
            Process p = process.start();
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
            return bri;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GraphModel showOneG(String line) {
        String g = "";

        try {
            String cur = new java.io.File(".").getCanonicalPath();

            if(System.getProperty("os.name").contains("Win")) {
                cur =cur + "\\graphs\\";
            } else {
                cur =cur + "/graphs/";
            }
            FileWriter fw = new FileWriter(cur+"tmpF.g6");
            fw.write(line);
            fw.write("\n");
            fw.close();
            ProcessBuilder process;
            if(System.getProperty("os.name").contains("Win")) {
                process = new ProcessBuilder(cur + "showg_win32.exe", cur + "tmpF.g6");
            } else {
                process = new ProcessBuilder(cur + "showg.out", cur + "tmpF.g6");
            }

            Process p = process.start();
            p.waitFor();
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            p.waitFor();
            Scanner sc = new Scanner(bri);
            sc.nextLine();
            GraphModel gg = parseGraph(sc);
            Point pp[] = PositionGenerators.circle(200, 400, 250, gg.numOfVertices());
            int tmpcnt = 0;
            for (Vertex v : gg) {
                v.setLocation(pp[tmpcnt]);
                tmpcnt++;
            }
            return gg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static GraphModel parseGraph(Scanner sc) {
        GraphModel g = new GraphModel();
        g.setDirected(false);
        //sc.nextLine();
        String order = sc.nextLine();
        order = order.substring(order.lastIndexOf("r") + 1,
                order.lastIndexOf("."));
        order = order.trim();

        int ord = Integer.parseInt(order);
        for (int i = 0; i < ord; i++) g.addVertex(new Vertex());
        for (int i = 0; i < ord; i++) {
            String tmp = sc.nextLine();
            tmp = tmp.substring(tmp.indexOf(":") + 1);
            Scanner sc2 = new Scanner(tmp.trim());
            while (sc2.hasNext()) {
                String num = sc2.next();
                if (num.contains(";")) num = num.substring(0, num.indexOf(";"));
                int id = Integer.parseInt(num);
                if (!g.isEdge(g.getVertex(i), g.getVertex(id)))
                    g.addEdge(new Edge(g.getVertex(i), g.getVertex(id)));
            }
        }

        return g;
    }
}
