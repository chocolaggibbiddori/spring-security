package chocola.security.authentication.admin.service.impl;

import chocola.security.authentication.admin.repository.ResourcesRepository;
import chocola.security.authentication.admin.service.ResourcesService;
import chocola.security.authentication.domain.entity.Resources;
import chocola.security.authentication.manager.DynamicAuthorizationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository resourcesRepository;
    private final DynamicAuthorizationManager dynamicAuthorizationManager;

    public Resources getResources(long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    public List<Resources> getResources() {
        return resourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Transactional
    public void createResources(Resources resources) {
        resourcesRepository.save(resources);
        dynamicAuthorizationManager.reload();
    }

    @Transactional
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
        dynamicAuthorizationManager.reload();
    }
}
