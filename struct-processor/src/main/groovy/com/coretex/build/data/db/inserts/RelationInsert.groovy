package com.coretex.build.data.db.inserts

import com.coretex.build.data.items.ClassItem
import com.coretex.build.data.items.RelationItem
import com.coretex.build.data.items.traits.Insertable

class RelationInsert extends Insert<RelationData> {

    private UUID uuid

    @Override
    UUID getUuid() {
        return uuid
    }

    @Override
    Collection<String> commands() {
        List<String> commands = []
        commands.add(getQueryFactory().statementQueryFactory().insertInto(this))
        return commands
    }

    RelationInsert() {
        uuid = UUID.randomUUID()
    }

    RelationData createRelationData() {
        return new RelationData()
    }

    class RelationData implements RowItem<RelationItem> {
        private Insertable source
        private ClassItem sourceType
        private Insertable target
        private ClassItem targetType

        Insertable getSource() {
            return source
        }

        Insertable getTarget() {
            return target
        }

        ClassItem getSourceType() {
            return sourceType
        }

        ClassItem getTargetType() {
            return targetType
        }

        void setSource(Insertable source) {
            this.source = source
        }

        void setTarget(Insertable target) {
            this.target = target
        }

        void setSourceType(ClassItem sourceType) {
            this.sourceType = sourceType
        }

        void setTargetType(ClassItem targetType) {
            this.targetType = targetType
        }

        @Override
        RelationItem getItem() {
            return essentialItem as RelationItem
        }
    }
}
