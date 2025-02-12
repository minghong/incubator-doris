package org.apache.doris.nereids.processor.post.lazyMaterialize;

import org.apache.doris.nereids.processor.post.lazyMaterialize.MaterializeProbeVisitor.ProbeContext;
import org.apache.doris.nereids.trees.expressions.Expression;
import org.apache.doris.nereids.trees.expressions.SlotReference;
import org.apache.doris.nereids.trees.plans.Plan;
import org.apache.doris.nereids.trees.plans.algebra.Union;
import org.apache.doris.nereids.trees.plans.physical.PhysicalCatalogRelation;
import org.apache.doris.nereids.trees.plans.physical.PhysicalFileScan;
import org.apache.doris.nereids.trees.plans.physical.PhysicalLazyMaterialize;
import org.apache.doris.nereids.trees.plans.physical.PhysicalLazyMaterializeFileScan;
import org.apache.doris.nereids.trees.plans.physical.PhysicalLazyMaterializeOlapScan;
import org.apache.doris.nereids.trees.plans.physical.PhysicalOlapScan;
import org.apache.doris.nereids.trees.plans.physical.PhysicalProject;
import org.apache.doris.nereids.trees.plans.physical.PhysicalSetOperation;
import org.apache.doris.nereids.trees.plans.visitor.DefaultPlanRewriter;
import org.apache.doris.nereids.trees.plans.visitor.DefaultPlanVisitor;

import java.util.Optional;

public class MaterializeProbeVisitor extends DefaultPlanVisitor<MaterializeSource, ProbeContext> {
    public static class ProbeContext {
        public SlotReference slot;
    }

    @Override
    public MaterializeSource visit(Plan plan, ProbeContext context) {
        if (plan.getInputSlots().contains(context.slot)) {
                return null;
        }

        Plan next = null;
        for (Plan child : plan.children()) {
            if (child.getOutput().contains(context.slot)) {
                next = child;
                break;
            }
        }
        if (next == null) {
            return null;
        } else {
            return next.accept(this, context);
        }
    }

    @Override
    public MaterializeSource visitPhysicalCatalogRelation(PhysicalCatalogRelation relation, ProbeContext context) {
        if (relation.getOutput().contains(context.slot)) {
            return new MaterializeSource(relation, context.slot);
        }
        return null;
    }

    @Override
    public MaterializeSource visitPhysicalLazyMaterialize(PhysicalLazyMaterialize<? extends Plan> materialize, ProbeContext context) {
        return materialize.child().accept(this, context);
    }

    @Override
    public MaterializeSource visitPhysicalLazyMaterializeFileScan(PhysicalLazyMaterializeFileScan<? extends Plan> scan, ProbeContext context) {
        return scan.get;
    }

    @Override
    public MaterializeSource visitPhysicalLazyMaterializeOlapScan(PhysicalLazyMaterializeOlapScan<? extends Plan> scan, ProbeContext context) {
        return null;
    }

    @Override
    public MaterializeSource visitPhysicalSetOperation(PhysicalSetOperation setOperation, ProbeContext context) {
        if (setOperation instanceof Union) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public MaterializeSource visitPhysicalProject(PhysicalProject<? extends Plan> project, ProbeContext context) {
        return null;
    }

}
