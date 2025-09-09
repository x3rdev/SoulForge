package com.github.x3rdev.soul_forge.common.research;

import com.github.x3rdev.soul_forge.client.screen.ResearchTableScreen;
import com.github.x3rdev.soul_forge.common.registry.DatapackRegistry;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ResearchTree implements Comparable<ResearchTree> {

    private final Holder.Reference<Research> head;
    private final SortedSet<ResearchTree> children;

    private static ResearchTree instance;

    private ResearchTree(Holder.Reference<Research> head) {
        this.head = head;
        this.children = new TreeSet<>();
    }

    private ResearchTree(Holder.Reference<Research> head, SortedSet<ResearchTree> children) {
        this.head = head;
        this.children = new TreeSet<>(children);
    }

    public static ResearchTree getResearchTree() {
        if(instance == null) {
            instance = buildTree();
        }
        return instance;
    }

    private static ResearchTree buildTree() {
        // Creates a map of every research node to each of its children
        Map<Holder.Reference<Research>, Set<Holder.Reference<Research>>> parentToChildrenMap = new HashMap<>();
        RegistryAccess access = Minecraft.getInstance().level.registryAccess();
        List<Holder.Reference<Research>> activeResearch = access.lookup(DatapackRegistry.RESEARCH_KEY).orElseThrow()
                .listElements()
                .filter(researchReference -> !researchReference.value().inactive())
                .toList();

        activeResearch.forEach(research -> {
            Holder.Reference<Research> parent = access.holder(research.value().getParent(access).key()).orElseThrow();
            parentToChildrenMap.putIfAbsent(parent, new HashSet<>());
            parentToChildrenMap.get(parent).add(research);
        });
        ResearchTree tree = new ResearchTree(Research.getHeadResearch(access));
        fillChildren(tree, parentToChildrenMap);
        return tree;
    }

    private static void fillChildren(ResearchTree tree, Map<Holder.Reference<Research>, Set<Holder.Reference<Research>>> parentToChildrenMap) {
        for (Holder.Reference<Research> research : parentToChildrenMap.getOrDefault(tree.head, Set.of())) {
            tree.children.add(new ResearchTree(research));
        }
        for (ResearchTree researchTree : tree.children) {
            fillChildren(researchTree, parentToChildrenMap);
        }
    }

    public int pixelBreadth() {
        if(children.isEmpty()) {
            return ResearchTableScreen.ICON_SIZE;
        } else {
            int size = ResearchTableScreen.PADDING*(children.size()-1);
            for (ResearchTree child : children) {
                size += child.pixelBreadth();
            }
            return size;
        }
    }

    public int pixelDepth() {
        if(children.isEmpty()) {
            return ResearchTableScreen.ICON_SIZE;
        } else {
            int max = -1;
            for (ResearchTree child : children) {
                max = Math.max(max, child.pixelDepth());
            }
            return ResearchTableScreen.ICON_SIZE+ResearchTableScreen.PADDING+max;
        }

    }

    public Holder.Reference<Research> getHead() {
        return head;
    }

    public ImmutableSet<ResearchTree> getChildren() {
        return ImmutableSet.copyOf(children);
    }

    @Override
    public int compareTo(@NotNull ResearchTree o) {
        return this.head.getKey().compareTo(o.head.getKey());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ResearchTree other) {
            return this.head.equals(other.head) && this.children.equals(other.children);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, children);
    }
}