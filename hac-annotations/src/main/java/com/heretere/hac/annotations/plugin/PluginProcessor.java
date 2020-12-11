package com.heretere.hac.annotations.plugin;

import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.heretere.hac.annotations.plugin.Plugin")
@AutoService(Processor.class)
public class PluginProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Set<? extends Element> annotatedElements = env.getElementsAnnotatedWith(Plugin.class);

        if (annotatedElements.isEmpty()) {
            return false;
        }

        TypeElement typeElement = (TypeElement) annotatedElements.iterator().next();
        Plugin plugin = typeElement.getAnnotation(Plugin.class);

        Map<String, Object> data = Maps.newLinkedHashMap();

        data.put("name", plugin.name());
        data.put("version", plugin.version());
        data.put("main", typeElement.getQualifiedName().toString());

        if (!plugin.prefix().isEmpty()) {
            data.put("prefix", plugin.prefix());
        }

        data.put("api-version", plugin.apiVersion());

        if (plugin.softDepends().length != 0) {
            data.put("softdepend", Lists.newArrayList(plugin.softDepends()));
        }

        try {
            Yaml yaml = new Yaml();
            FileObject resource = this.processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml");

            try (Writer writer = resource.openWriter(); BufferedWriter bw = new BufferedWriter(writer)) {
                yaml.dump(data, bw);
                bw.flush();
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Cannot serialize plugin.yml: " + e.getMessage(), e);
        }
    }
}
