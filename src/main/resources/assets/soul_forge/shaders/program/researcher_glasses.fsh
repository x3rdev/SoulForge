#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

out vec4 fragColor;

void main(){
    float factor = 0.8;
    vec3 diffuseColor = texture(DiffuseSampler, texCoord).rgb;
    vec3 lum = vec3(0.299, 0.587, 0.114);
    vec3 gray = vec3(dot(lum, diffuseColor));
    fragColor = vec4(mix(diffuseColor, gray, factor), 1.0);
}
