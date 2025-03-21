package com.example.apigateway.filter;

import com.example.apigateway.config.RouteValidator;
import com.example.apigateway.properties.JwtProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
    private final RouteValidator routeValidator;
    private final JwtProperties jwtProperties;

    public JwtAuthFilter(RouteValidator routeValidator, JwtProperties jwtProperties) {
        this.routeValidator = routeValidator;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        log.info("📢 Request Path: {}", path);

        if (!routeValidator.isSecured.test(exchange.getRequest())) {
            log.info("🔓 Public route accessed: {}", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("❌  Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        log.info("🔑 Received Token: {}", token);

        try {
            Claims claims = validateToken(token, jwtProperties.getAccessSecret());
            String username = claims.getSubject();
            log.info("✅  Token valid for user: {}", username);

            ObjectMapper objectMapper = new ObjectMapper();
            List<String> roles = objectMapper.convertValue(
                    claims.get("role", List.class), new TypeReference<List<String>>() {}
            );

            log.info("📦 User Roles: {}", roles);

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Roles", String.join(",", roles))
                    .build();
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange);
        } catch (JwtException e) {
            log.error("❌  JWT ERROR: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Claims validateToken(String token, String secretKey) {
        Jwt<?, Claims> jwt = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseSignedClaims(token);
        return jwt.getPayload();
    }
}
