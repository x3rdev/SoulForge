#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;

    vec2 tileUV = fract(texCoord0 * vec2(32.0, 16.0));
    vec2 centered = tileUV - vec2(0.5, 0.5);
    float dist = length(centered);
    float radius = 0.25;
    float edge = 0.08;
    float alphaFade = smoothstep(radius, radius - edge, dist);
    float glowOuter = radius + 0.1;
    float glowInner = radius;
    float glowAmount = smoothstep(glowOuter, glowInner, dist);
    vec3 glowColor = vec3(1.0);
    fragColor.rgb += glowColor * glowAmount * 0.4;

    color.a *= alphaFade;

    if (color.a < 0.1) {
        discard;
    }

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
