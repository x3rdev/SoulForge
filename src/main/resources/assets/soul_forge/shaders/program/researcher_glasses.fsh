#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D EntityMaskSampler;

uniform vec2 ScreenSize;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 baseColor = texture(DiffuseSampler, texCoord);
    vec4 mask      = texture(EntityMaskSampler, texCoord);

    vec2 texelSize = 1.0 / ScreenSize;

    float edge = 0.0;
    for (int x = -2; x <= 2; x++) {
        for (int y = -2; y <= 2; y++) {
            if (x == 0 && y == 0) continue; // skip center
            edge += texture(EntityMaskSampler, texCoord + vec2(x, y) * texelSize).a;
        }
    }

    if (mask.a > 0.01) {
        fragColor = baseColor;
    } else if (edge > 0.0) {
        fragColor = vec4(0.373, 0.639, 1.0, 1.0);
    } else {
        float factor = 0.8;
        vec3 lum = vec3(0.299, 0.587, 0.114);
        vec3 gray = vec3(dot(lum, baseColor.rgb));
        fragColor = vec4(mix(baseColor.rgb, gray, factor), baseColor.a);
    }
}
