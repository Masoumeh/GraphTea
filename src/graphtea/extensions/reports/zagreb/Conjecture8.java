// GraphTea Project: http://github.com/graphtheorysoftware/GraphTea
// Copyright (C) 2012 Graph Theory Software Foundation: http://GraphTheorySoftware.com
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphtea.extensions.reports.zagreb;

import graphtea.graph.graph.Edge;
import graphtea.graph.graph.RendTable;
import graphtea.graph.graph.Vertex;
import graphtea.platform.lang.CommandAttitude;
import graphtea.platform.parameter.Parameter;
import graphtea.platform.parameter.Parametrizable;
import graphtea.plugins.main.GraphData;
import graphtea.plugins.reports.extension.GraphReportExtension;

import java.util.Vector;

/**
 * @author Ali Rostami

 */

@CommandAttitude(name = "conj7", abbreviation = "_conj7")
public class Conjecture8 implements GraphReportExtension,Parametrizable {
    public String getName() {
        return "ZIndices of Matching Conjecture 7";
    }

    @Parameter(name = "Starting Value of Alpha", description = "")
    public Double start_alpha = 0.0;

    @Parameter(name = "End Value of Alpha", description = "")
    public Double end_alpha = 10.0;

    @Parameter(name = "Incremental Value", description = "")
    public Double inc = 0.1;

    public String getDescription() {
        return "ZIndices of Matching Conjecture 7";
    }

    public Object calculate(GraphData gd) {
        ZagrebIndexFunctions zif = new ZagrebIndexFunctions(gd);
        RendTable ret = new RendTable();
        ret.add(new Vector<Object>());
        ret.get(0).add("Alpha");
        ret.get(0).add(" 2*M^{a}_2 (M) ");
        ret.get(0).add(" M^{a+1}_1 (M)");
        ret.get(0).add(" (2a_m+b_m+2k/2k)^(a)M^{a}_1 (M)");
        ret.get(0).add(" (2a_m+b_m+2k/2k)^(a-1)M^{a+1}_1 (M)");
        double k = 0;
        for(Edge e : gd.getGraph().getEdges()) {
            if(e.isSelected()) k++;
        }

        double am = 0;
        double bm = 0;
        for(Edge e : gd.getGraph().getEdges()) {
            if(!e.isSelected()) {
                Vertex s = e.source;
                Vertex t = e.target;

                Boolean isSourceInMatching = false;
                Boolean isTargetInMatching = false;
                for(Vertex n : gd.getGraph().getNeighbors(s)) {
                    if(gd.getGraph().getEdge(s,n).isSelected()) {
                        isSourceInMatching = true;
                        break;
                    }
                }

                for(Vertex n : gd.getGraph().getNeighbors(t)) {
                    if(gd.getGraph().getEdge(t,n).isSelected()) {
                        isTargetInMatching = true;
                        break;
                    }
                }

                if(isSourceInMatching && isTargetInMatching) {
                    am++;
                }

                else if(isSourceInMatching || isTargetInMatching) {
                    bm++;
                }
            }
        }

        int ind = 0;
        for(double alpha = start_alpha;alpha <= end_alpha;alpha=alpha+inc) {
            ind++;
            double sZagreb=zif.getSecondZagrebSelectedEdges(alpha);
            double fZagreb=zif.getFirstZagrebSelectedEdges(alpha);
            double fZagreb1=zif.getFirstZagrebSelectedEdges(alpha-1);
            double coef = (2*am + bm + 2*k)/(2*k);
            ret.add(new Vector<Object>());
            ret.get(ind).add(alpha);
            ret.get(ind).add(2*sZagreb);
            ret.get(ind).add(fZagreb);
            ret.get(ind).add(Math.pow(coef,alpha)*fZagreb1);
            ret.get(ind).add(Math.pow(coef,alpha-1)*fZagreb);
        }

        return ret;
    }

    @Override
	public String getCategory() {
		// TODO Auto-generated method stub
		return "Topological Indices";
	}

    @Override
    public String checkParameters() {
        return null;
    }
}
