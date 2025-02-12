package org.apache.doris.nereids.processor.post.lazyMaterialize;

import org.apache.doris.nereids.jobs.JobContext;
import org.apache.doris.nereids.trees.expressions.Slot;
import org.apache.doris.nereids.trees.expressions.SlotReference;
import org.apache.doris.nereids.trees.plans.Plan;
import org.apache.doris.nereids.trees.plans.physical.PhysicalTopN;

public class LazyMaterializeTopN {
    public Plan rewriteTopN(PhysicalTopN topN) {
        for (Slot slot : topN.getOutput()) {

        }
        return topN;
    }

    private MaterializeSource canLazyMaterialize(Plan plan, SlotReference slot) {
        return null;
    }

}
