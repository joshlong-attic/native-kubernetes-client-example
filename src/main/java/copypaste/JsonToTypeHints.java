package copypaste;

import bootiful.kubernetesclientexample.KubernetesClientExampleApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonToTypeHints {

    @SneakyThrows
    public static void main(String[] args) {
        var om = new ObjectMapper();
        var list = new ArrayList<String>();
        var resource = new FileSystemResource("/Users/jlong/Desktop/spring-controller/" +
                "src/main/resources/META-INF/native-image/reflect-config.json");
        try (var in = resource.getInputStream()) {
            var parser = om.createParser(in);
            var tree = parser.readValueAsTree();
            var size = tree.size();
            System.out.println("the size is: " + size);
            for (var i = 0; i < tree.size(); i++) {
                var item = tree.get(i);
                var name = item.get("name");
                var txt = ((TextNode) name).asText();
                process(list, txt);
            }
        }
        var setOfAnnotations = list
                .stream()
                .map(JsonToTypeHints::buildAnnotationFrom)
                .collect(Collectors.toSet());
        setOfAnnotations.forEach(System.out::println);
    }

    private static void process(List<String> total, String context) {
        total.add(context);
    }


    private static String buildAnnotationFrom(String className) {
        var str = String.format(
                "@TypeHint (typeNames = { \"%s\"   }, access = AccessBits.ALL)", className);
        return str.trim();
    }

}
