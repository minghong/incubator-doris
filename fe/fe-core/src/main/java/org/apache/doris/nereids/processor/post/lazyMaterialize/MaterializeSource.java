package org.apache.doris.nereids.processor.post.lazyMaterialize;

import org.apache.doris.nereids.trees.expressions.SlotReference;
import org.apache.doris.nereids.trees.plans.algebra.CatalogRelation;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MaterializeSource {
    public static class SourceSlot {
        public CatalogRelation relation;
        public SlotReference baseSlot;

        public SourceSlot(CatalogRelation relation, SlotReference baseSlot) {
            this.relation = relation;
            this.baseSlot = baseSlot;
        }
    }

    @Getter
    @Setter
    SlotReference rowId;

    List<SourceSlot> sourceSlots = Lists.newArrayList();

    public MaterializeSource(CatalogRelation relation, SlotReference baseSlot) {
        sourceSlots.add(new SourceSlot(relation, baseSlot));
    }
}
