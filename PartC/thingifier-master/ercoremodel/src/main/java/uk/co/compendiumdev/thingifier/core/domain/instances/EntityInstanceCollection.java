package uk.co.compendiumdev.thingifier.core.domain.instances;

import uk.co.compendiumdev.thingifier.core.EntityRelModel;
import uk.co.compendiumdev.thingifier.core.domain.definitions.field.definition.Field;
import uk.co.compendiumdev.thingifier.core.domain.definitions.field.definition.FieldType;
import uk.co.compendiumdev.thingifier.core.domain.definitions.field.instance.FieldValue;
import uk.co.compendiumdev.thingifier.core.domain.definitions.EntityDefinition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


final public class EntityInstanceCollection {

    private final EntityDefinition definition;
    private Map<String, EntityInstance> instances = new ConcurrentHashMap<>();

    public EntityInstanceCollection(EntityDefinition thingDefinition) {
        this.definition = thingDefinition;
    }

    public EntityInstanceCollection(final EntityDefinition entity, final List<EntityInstance> instances) {
       this.definition=entity;
       addInstances(instances);
    }

    public EntityInstanceCollection addInstances(List<EntityInstance> instances) {
        for(EntityInstance instance : instances){
            addInstance(instance);
        }
        return this;
    }

    public EntityInstanceCollection addInstance(EntityInstance instance) {

        if(instance.getEntity()!=definition){
            throw new RuntimeException(String.format(
                    "ERROR: Tried to add a %s instance to the %s",
                    instance.getEntity().getName(), definition.getName()));
        }

        instances.put(instance.getGUID(), instance);
        return this;
    }

    /* create and add */
    public EntityInstance createManagedInstance() {
        EntityInstance instance = new EntityInstance(definition);
        instance.addGUIDtoInstance();
        instance.addIdsToInstance();
        addInstance(instance);
        return instance;
    }

    public int countInstances() {
        return instances.size();
    }


    public EntityInstance findInstanceByField(FieldValue fieldValue) {

        for (EntityInstance thing : instances.values()) {
            if (thing.getFieldValue(fieldValue.getName())
                    .asString().contentEquals(fieldValue.asString())) {
                return thing;
            }
        }

        return null;
    }

    public EntityInstance findInstanceByGUID(String instanceFieldValue) {

        if (instances.containsKey(instanceFieldValue)) {
            return instances.get(instanceFieldValue);
        }

        return null;
    }


    public Collection<EntityInstance> getInstances() {
        return instances.values();
    }


    /**
     * This deletes the instance but does not delete any mandatorily related items, these need to be handled by
     * another class using the returned list of alsoDelete, otherwise the model will be invalid
     *
     * @param guid
     * @return
     */
    public List<EntityInstance> deleteInstance(String guid) {

        if (!instances.containsKey(guid)) {
            throw new IndexOutOfBoundsException(
                    String.format("Could not find a %s with GUID %s",
                            definition.getName(), guid));
        }

        EntityInstance item = instances.get(guid);

        instances.remove(guid);

        final List<EntityInstance> alsoDelete = item.getRelationships().removeAllRelationships();


        return alsoDelete;
    }

    /*

        Definition abstractions

     */

    public EntityDefinition definition() {
        return definition;
    }

    private List<String> getGuidList() {
        List<String> guids = new ArrayList<>();
        for(EntityInstance instance : instances.values()){
            guids.add(instance.getGUID());
        }

        return guids;
    }


    // todo: not comfortable with this method, we should be using specific field names
    public EntityInstance findInstanceByGUIDorID(final String instanceGuid) {
        EntityInstance instance = findInstanceByGUID(instanceGuid);
        if(instance==null){
            final List<Field> idFields = definition.getFieldsOfType(FieldType.ID);
            if(!idFields.isEmpty()) {
                instance = findInstanceByField(
                        FieldValue.is(
                                (idFields.get(0)).getName(),
                                instanceGuid));
            }
        }
        return instance;
    }

}
