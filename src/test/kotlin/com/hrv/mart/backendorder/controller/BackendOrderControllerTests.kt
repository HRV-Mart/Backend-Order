package com.hrv.mart.backendorder.controller

import com.hrv.mart.backendorder.fixture.TestFixture.orderResponses
import com.hrv.mart.backendorder.fixture.TestFixture.orders
import com.hrv.mart.backendorder.fixture.TestFixture.productOrders
import com.hrv.mart.backendorder.fixture.TestFixture.users
import com.hrv.mart.backendorder.model.OrderQuery
import com.hrv.mart.backendorder.repository.OrderRepository
import com.hrv.mart.backendorder.repository.ProductOrderedRepository
import com.hrv.mart.backendorder.service.OrderService
import com.hrv.mart.custompageable.Pageable
import com.hrv.mart.orderlibrary.model.OrderRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.testcontainers.containers.MongoDBContainer
import reactor.test.StepVerifier
import java.util.Optional

@DataMongoTest
class BackendOrderControllerTests (
    @Autowired
    private val orderRepository: OrderRepository,
    @Autowired
    private val productOrderedRepository: ProductOrderedRepository
) {
    private val response = mock(ServerHttpResponse::class.java)
    private val orderService = OrderService(
        orderRepository,
        productOrderedRepository
    )
    private val orderController = OrderController(orderService)

    @BeforeEach
    fun addDataInDatabase() {
        orderRepository
            .deleteAll()
            .block()
        orderRepository
            .insert(orders)
            .subscribe()
        productOrderedRepository.
            deleteAll()
            .block()
        productOrderedRepository
            .insert(productOrders)
            .subscribe()
    }

    @Test
    fun `should get user orders`() {
        val userId = users.random()
        val expected = orderResponses
            .filter {
                it.userId == userId
            }
        StepVerifier.create(
            orderController
                .getUserOrders(userId, Optional.empty(), Optional.empty())
        )
            .expectNext(
                Pageable(
                    size = 10,
                    data = expected,
                    nextPage = null
                )
            )
            .verifyComplete()
    }
    @Test
    fun `should get order by orderId and userId`() {
        val userId = users.random()
        val expected = orderResponses
            .filter {
                it.userId == userId
            }
            .random()
        val orderId = expected.orderId
        StepVerifier.create(
            orderController
                .getOrderByUserIdAndOrderId(userId, orderId, response)
        )
            .expectNext(
                expected
            )
            .verifyComplete()
    }
    @Test
    fun `should get orders by applying filtering`() {
        val expected = orders
        StepVerifier.create(
            orderController
                .applyFilteringAndSorting(OrderQuery(), Optional.empty(), Optional.empty())
        )
            .expectNext(Pageable(
                size = 10,
                nextPage = null,
                data = expected
            ))
            .verifyComplete()
    }
    @Test
    fun `should add order in db`() {
        val order = orderResponses.random()
        StepVerifier.create(
            orderController
                .addCartToOrder(
                    OrderRequest(
                        userId = order.userId,
                        products = order.products,
                        price = order.price
                    )
                )
        )
            .verifyComplete()
    }

    companion object {
        private lateinit var mongoDBContainer: MongoDBContainer

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            mongoDBContainer = MongoDBContainer("mongo:latest")
                .apply { withExposedPorts(27_017) }
                .apply { start() }
            mongoDBContainer
                .withReuse(true)
                .withAccessToHost(true)
            System.setProperty("spring.data.mongodb.uri", "${mongoDBContainer.connectionString}/test")
            System.setProperty("spring.data.mongodb.auto-index-creation", "true")
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            mongoDBContainer.stop()
        }
    }

}