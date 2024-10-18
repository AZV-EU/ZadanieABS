package dev.azv.zadanieabs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.azv.zadanieabs.data.product.ProductsRepository
import dev.azv.zadanieabs.data.user.UserRepository
import dev.azv.zadanieabs.domain.products.RemoteProductsRepository
import dev.azv.zadanieabs.domain.user.UserRepositoryImpl
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun defaultProductsRepository(productsRepository: RemoteProductsRepository): ProductsRepository

    @Binds
    @RemoteRepository
    fun remoteProductsRepository(remoteRepository: RemoteProductsRepository): ProductsRepository

    @Binds
    fun userRepository(userRepository: UserRepositoryImpl): UserRepository
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteRepository